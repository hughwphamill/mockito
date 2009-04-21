package org.mockitousage.stubbing;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

/**
 * Description: TODO: Enter a paragraph that summarizes what the class does and
 * why someone might want to utilize it
 * 
 * <p>
 * Copyright � 2000-2007, NetSuite, Inc.
 * </p>
 * 
 * @author amurkes
 * @version 2007.0
 * @since Apr 16, 2009
 */
public class CallingRealMethodTest extends TestBase {
    
    @Mock UnderTest mock;

    static class UnderTest {
        String value;

        void setValue(String value) {
            this.value = value;
        }

        String getValue() {
            return "HARD_CODED_RETURN_VALUE";
        }
        
        String callInternalMethod() {
            return getValue();
        }
    }
    
    @Test
    public void shouldAllowCallingInternalMethod() {
        when(mock.getValue()).thenReturn("foo");
        when(mock.callInternalMethod()).thenCallRealMethod();
        
        assertEquals("foo", mock.callInternalMethod());
    }

    @Test
    public void shouldReturnRealValue() {
        when(mock.getValue()).thenCallRealMethod();

        Assert.assertEquals("HARD_CODED_RETURN_VALUE", mock.getValue());
    }

    @Test
    public void shouldExecuteRealMethod() {
        doCallRealMethod().when(mock).setValue(anyString());

        mock.setValue("REAL_VALUE");

        Assert.assertEquals("REAL_VALUE", mock.value);
    }

    @Test
    public void shouldCallRealMethodByDefault() {
        UnderTest mock = mock(UnderTest.class, CALLS_REAL_METHODS);

        Assert.assertEquals("HARD_CODED_RETURN_VALUE", mock.getValue());
    }

    @Test
    public void shouldNotCallRealMethodWhenStubbedLater() {
        UnderTest mock = mock(UnderTest.class);

        when(mock.getValue()).thenCallRealMethod();
        when(mock.getValue()).thenReturn("FAKE_VALUE");

        Assert.assertEquals("FAKE_VALUE", mock.getValue());
    }
}