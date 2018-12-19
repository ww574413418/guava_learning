package zc.com.guava_learning.demo.utilities;

import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Grubby
 * @date 2018/12/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SplitterTest {

    @Test
    public void testSplitter(){
        List<String> result = Splitter.on("|").splitToList("hello|world");

        assertThat(result,notNullValue());
        assertThat(result.size(),equalTo(2));
        assertThat(result.get(0),equalTo("hello"));
        assertThat(result.get(1),equalTo("world"));
    }

    @Test
    public void testSplitter_On_Splitter_OmitEmpty(){
        // 很多的分隔符 "|" 后面是空的
        String str = "hello|world||||";
        List<String> result = Splitter.on("|").omitEmptyStrings().splitToList(str);

        assertThat(result,notNullValue());
        assertThat(result.size(),equalTo(2));
        assertThat(result.get(0),equalTo("hello"));
        assertThat(result.get(1),equalTo("world"));
    }

    /**
     * 去除文字前后空格
     */
    @Test
    public void testSplitter_On_OmitEmpty_TrimResult(){
        // 很多的分隔符 "|" 后面是空的 而且有前后空格
        String str = " hello|world ||||";
        List<String> result = Splitter.on("|").trimResults().omitEmptyStrings().splitToList(str);

        assertThat(result,notNullValue());
        assertThat(result.size(),equalTo(2));
        assertThat(result.get(0),equalTo("hello"));
        assertThat(result.get(1),equalTo("world"));
    }

    /**
     *  aaaabbbbcccc -> aaaa   bbbb   cccc
     */
    @Test
    public void testSplitterFixLength(){
        String str = "aaaabbbbcccc";
        List<String> result = Splitter.fixedLength(4).splitToList(str);

        assertThat(result,notNullValue());
        assertThat(result.size(),equalTo(3));
        assertThat(result.get(0),equalTo("aaaa"));
        assertThat(result.get(2),equalTo("cccc"));
    }

    /**
     * hello#world#java#google#groovy  -> hello  world  java#google#groovy
     */
    @Test
    public void testSplitterOnLimit(){
        String str = "hello#world#java#google#groovy";
        List<String> result = Splitter.on("#").limit(3).splitToList(str);

        assertThat(result,notNullValue());
        assertThat(result.size(),equalTo(3));
        assertThat(result.get(0),equalTo("hello"));
        assertThat(result.get(2),equalTo("java#google#groovy"));
    }

    /**
     * 使用 正则表达式
     */
    @Test
    public void testSplitter_pattern(){
        // 直接传入正则
        List<String> result = Splitter.onPattern("\\|").trimResults().omitEmptyStrings().splitToList(" hello|world ||||");
        // 传入 pattern
        List<String> result2 = Splitter.on(Pattern.compile("\\|")).trimResults().omitEmptyStrings().splitToList(" hello|world ||||");

        assertThat(result,notNullValue());
        assertThat(result.size(),equalTo(2));
        assertThat(result.get(0),equalTo("hello"));
        assertThat(result.get(1),equalTo("world"));
    }


    /**
     * 将正则拆的 value 放入 map 中
     */
    @Test
    public void testSplitter_toMap(){
        Map<String, String> result = Splitter.on(Pattern.compile("\\|")).trimResults()
                .omitEmptyStrings().withKeyValueSeparator("=").split(" hello=Hello|world=World ||||");

        assertThat(result,notNullValue());
        assertThat(result.size(),equalTo(2));
        System.out.println(result.toString());
    }
}
