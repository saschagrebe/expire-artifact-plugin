package ut.de.sagr.ci.bamboo;

import org.junit.Test;
import de.sagr.ci.bamboo.api.MyPluginComponent;
import de.sagr.ci.bamboo.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}