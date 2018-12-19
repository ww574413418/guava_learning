package zc.com.guava_learning.demo.utilities;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Grubby
 * @date 2018/12/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JoinerTest {

    private final List<String> stringList = Arrays.asList(
                "Google","Guava","Java","Scala","Kafka"
            );

    private final List<String> stringListWitnNullValue = Arrays.asList(
            "Google","Guava","Java","Scala",null
            );

    private final Map<String,String> stringMap = ImmutableMap.of("hello","Guava","java","Scala");

    private final String targetFilePath = "/Users/zhangchao/Desktop/classloader/guave-joiner.txt";
    private final String targetFilePath01 = "/Users/zhangchao/Desktop/classloader/guave-joiner-map.txt";

    @Test
    public void testJoinToJoin(){
        String result = Joiner.on("#").join(stringList);
        assertThat(result,equalTo("Google#Guava#Java#Scala#Kafka"));
    }

    /**
     * (expected = NullPointerException.class)
     * 如果 报错 NullPointerException 绿条
     * 如果不是 报错 NullPointerException 红条
     */
    @Test(expected = NullPointerException.class)
    public void testJoinToJoinWithNullValue(){
        String result = Joiner.on("#").join(stringListWitnNullValue);
        assertThat(result,equalTo("Google#Guava#Java#Scala"));
    }

    @Test
    public void testJoinToJoinWithNullValueButSkip(){
        String result = Joiner.on("#").skipNulls().join(stringListWitnNullValue);
        assertThat(result,equalTo("Google#Guava#Java#Scala"));
    }

    @Test
    public void testJoinToJoin_With_NullValue_But_UserDefaulValue(){
        String result = Joiner.on("#").useForNull("DEFAULT").join(stringListWitnNullValue);
        assertThat(result,equalTo("Google#Guava#Java#Scala#DEFAULT"));
    }

    /**
     *  builder 和 result 的StringBuilder 是同一个对象
     */
    @Test
    public void testJoinToJoin_On_Append_To_StringBuilder(){
        final StringBuilder builder = new StringBuilder();
        StringBuilder result = Joiner.on("#").useForNull("DEFAULT").appendTo(builder, stringListWitnNullValue);
        assertThat(result,sameInstance(builder));
        assertThat(result.toString(),equalTo("Google#Guava#Java#Scala#DEFAULT"));
    }

    /**
     * 将拼接的字符串写到文件中
     */
    @Test
    public void testJoinToJoin_On_Append_To_Writer(){
        try (FileWriter fileWriter = new FileWriter(new File(targetFilePath))){
            Joiner.on("#").useForNull("DEFAULT").appendTo(fileWriter,stringListWitnNullValue);
            // 判断文件是否存在
            assertThat(Files.isFile().test(new File(targetFilePath)),equalTo(true));
        }catch (IOException e){
            fail("append to the writer occue fetal error.");
        }
    }

    @Test
    public void testJoiningByStream(){
        String result = stringListWitnNullValue.stream().filter(item -> item != null && !item.isEmpty()).collect(joining("#"));
        assertThat(result,equalTo("Google#Guava#Java#Scala"));
    }

    @Test
    public void testJoiningByStream_UserDefaultValue1(){
        String result = stringListWitnNullValue.stream().map(item -> item == null || item.isEmpty() ? "DEFAULT" : item).collect(joining("#"));
        assertThat(result,equalTo("Google#Guava#Java#Scala#DEFAULT"));
    }

    @Test
    public void testJoiningByStream_UserDefaultValue2(){
        String result = stringListWitnNullValue.stream().map(this :: defaultValue).collect(joining("#"));
        assertThat(result,equalTo("Google#Guava#Java#Scala#DEFAULT"));
    }

    private String defaultValue(String item) {
        return item == null || item.isEmpty() ? "DEFAULT" : item;
    }

    @Test
    public void testJoinOnMap(){
        String result = Joiner.on("#").withKeyValueSeparator("=").join(stringMap);
        assertThat(result,equalTo("hello=Guava#java=Scala"));

        try (FileWriter fileWriter = new FileWriter(targetFilePath01)){
            Joiner.on("#").withKeyValueSeparator("=").appendTo(fileWriter,stringMap);
            assertThat(Files.isFile().test(new File(targetFilePath01)),equalTo(true));
        }catch (IOException e){
            fail("append to the writer occue fetal error.");
        }
    }
}
