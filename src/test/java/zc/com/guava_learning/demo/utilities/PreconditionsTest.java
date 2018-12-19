package zc.com.guava_learning.demo.utilities;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import java.util.List;
import java.util.Objects;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Guava 的 断言
 * @author Grubby
 * @date 2018/12/17
 */
public class PreconditionsTest {

    @Test
    public void testCheckNotNull(){
        checkNotNull(null);
    }

    @Test
    public void testCheckNotNullWithMessage(){
        try {
            checkNotNullWithMessage(null);
        }catch (Exception e){
            //assertThat(e, is(NullPointerException.class));
            assertThat(e.getMessage(),equalTo("The list should not been null"));
        }

    }

    @Test
    public void testCheckNotNullWithFormatMessage(){
        try {
            checkNotNullWithFormateMessage(null);
        }catch (Exception e){
            //assertThat(e, is(NullPointerException.class));
            //assertThat(e.getMessage(),equalTo("The list should not been null"));
            e.printStackTrace();
        }

    }

    /**
     * 校验元素
     */
    @Test
    public void testCheckArguments(){
        final String type = "A";
        try {
            Preconditions.checkArgument(type.equals("B"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 检查状态
     */
    @Test
    public void testCheckState(){
        try {
            final String type = "a";
            Preconditions.checkState(type.equals("c"),"the state is error");
            fail("Can not process to here");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckIndex(){
        try {
            List<String> list = ImmutableList.of();
            Preconditions.checkElementIndex(10,list.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 用 java 自带的工具类来进行校验
     */
    @Test(expected = AssertionError.class)
    public void testAssert(){
        List<String> list = null;
        assert list != null;
    }

    /**
     * 用 java 自带的工具类来进行校验
     */
    @Test
    public void testAssertWithMessage(){
        List<String> list = null;
        assert list != null : "list can be null";
    }

    /**
     * 用 java 自带的工具类来进行校验
     */
    @Test
    public void testByObjects(){
        Objects.requireNonNull(null,"The value was required not null");
    }


    /**
     * 校验集合不能为 null
     * @param list 集合
     */
    private void checkNotNull(final List<String> list){
        Preconditions.checkNotNull(list);
    }

    /**
     * 校验集合不能为 null 并输出自定义错误信息
     * @param list 集合
     */
    private void checkNotNullWithMessage(final List<String> list){
        Preconditions.checkNotNull(list,"The list should not been null");
    }

    /**
     * 校验集合不能为 null 并输出自定义错误信息(有格式化)
     * @param list 集合
     */
    private void checkNotNullWithFormateMessage(final List<String> list){
        Preconditions.checkNotNull(list,"The list shloud not been null and the size must be %s",2);
    }

}
