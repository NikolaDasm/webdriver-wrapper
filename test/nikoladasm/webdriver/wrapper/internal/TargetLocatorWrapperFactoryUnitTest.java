/*
 *  WebDriver Wrapper
 *  Copyright (C) 2016  Nikolay Platov
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package nikoladasm.webdriver.wrapper.internal;

import static nikoladasm.webdriver.wrapper.EventListenerLocation.*;
import static nikoladasm.webdriver.wrapper.internal.TargetLocatorWrapperFactory.wrapTargetLocator;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.InOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import nikoladasm.webdriver.wrapper.UnderlyingWebDriver;
import nikoladasm.webdriver.wrapper.listeners.OnExceptionEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSwitchToActiveElementAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSwitchToActiveElementBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSwitchToAlertAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSwitchToAlertBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSwitchToDefaultContentEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSwitchToFrameEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSwitchToParentFrameEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSwitchToWindowEventListener;

public class TargetLocatorWrapperFactoryUnitTest extends ListenableBaseTest {

	private final TargetLocator uTargetLocator = mock(TargetLocator.class);
	private final UnderlyingWebDriver uDriver = mock(UnderlyingWebDriver.class);
	private final UnderlyingWebDriver wDriver = mock(UnderlyingWebDriver.class);
	
	private TargetLocator wTargetLocator = wrapTargetLocator(uTargetLocator, uDriver, listeners, wDriver);
	
	@Test
	public void shouldBeReturnWrappedTargetLocator() {
		assertThat(((WrapsTargetLocator) wTargetLocator).getWrappedTargetLocator(), is(equalTo(uTargetLocator)));
	}
	
	@Test
	public void shouldBeFireOnSwitchToFrameListener() {
		WebElement element = mock(WebElement.class);
		when(uTargetLocator.frame(1)).thenReturn(uDriver);
		when(uTargetLocator.frame("testFrame")).thenReturn(uDriver);
		when(uTargetLocator.frame(element)).thenReturn(uDriver);
		OnSwitchToFrameEventListener before = mock(OnSwitchToFrameEventListener.class, CALLS_REAL_METHODS);
		OnSwitchToFrameEventListener after = mock(OnSwitchToFrameEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_SWITCH_TO_FRAME, before);
		setListener(AFTER_SWITCH_TO_FRAME, after);
		InOrder inOrder = inOrder(before, uTargetLocator, after);
		assertThat(wTargetLocator.frame(1), is(equalTo(wDriver)));
		assertThat(wTargetLocator.frame("testFrame"), is(equalTo(wDriver)));
		assertThat(wTargetLocator.frame(element), is(equalTo(wDriver)));
		inOrder.verify(before).onSwitchToFrame(uDriver, uTargetLocator, 1);
		inOrder.verify(uTargetLocator).frame(1);
		inOrder.verify(after).onSwitchToFrame(uDriver, uTargetLocator, 1);
		inOrder.verify(before).onSwitchToFrame(uDriver, uTargetLocator, "testFrame");
		inOrder.verify(uTargetLocator).frame("testFrame");
		inOrder.verify(after).onSwitchToFrame(uDriver, uTargetLocator, "testFrame");
		inOrder.verify(before).onSwitchToFrame(uDriver, uTargetLocator, element);
		inOrder.verify(uTargetLocator).frame(element);
		inOrder.verify(after).onSwitchToFrame(uDriver, uTargetLocator, element);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnSwitchToWindowListener() {
		when(uTargetLocator.window("testWindow")).thenReturn(uDriver);
		OnSwitchToWindowEventListener before = mock(OnSwitchToWindowEventListener.class, CALLS_REAL_METHODS);
		OnSwitchToWindowEventListener after = mock(OnSwitchToWindowEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_SWITCH_TO_WINDOW, before);
		setListener(AFTER_SWITCH_TO_WINDOW, after);
		InOrder inOrder = inOrder(before, uTargetLocator, after);
		assertThat(wTargetLocator.window("testWindow"), is(equalTo(wDriver)));
		inOrder.verify(before).onSwitchToWindow(uDriver, uTargetLocator, "testWindow");
		inOrder.verify(uTargetLocator).window("testWindow");
		inOrder.verify(after).onSwitchToWindow(uDriver, uTargetLocator, "testWindow");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnSwitchToDefaultContextListener() {
		when(uTargetLocator.defaultContent()).thenReturn(uDriver);
		OnSwitchToDefaultContentEventListener before = mock(OnSwitchToDefaultContentEventListener.class, CALLS_REAL_METHODS);
		OnSwitchToDefaultContentEventListener after = mock(OnSwitchToDefaultContentEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_SWITCH_TO_DEFAULT_CONTENT, before);
		setListener(AFTER_SWITCH_TO_DEFAULT_CONTENT, after);
		InOrder inOrder = inOrder(before, uTargetLocator, after);
		assertThat(wTargetLocator.defaultContent(), is(equalTo(wDriver)));
		inOrder.verify(before).onSwitchToDefaultContent(uDriver, uTargetLocator);
		inOrder.verify(uTargetLocator).defaultContent();
		inOrder.verify(after).onSwitchToDefaultContent(uDriver, uTargetLocator);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnSwitchToParentFrameListener() {
		when(uTargetLocator.parentFrame()).thenReturn(uDriver);
		OnSwitchToParentFrameEventListener before = mock(OnSwitchToParentFrameEventListener.class, CALLS_REAL_METHODS);
		OnSwitchToParentFrameEventListener after = mock(OnSwitchToParentFrameEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_SWITCH_TO_PARENT_FRAME, before);
		setListener(AFTER_SWITCH_TO_PARENT_FRAME, after);
		InOrder inOrder = inOrder(before, uTargetLocator, after);
		assertThat(wTargetLocator.parentFrame(), is(equalTo(wDriver)));
		inOrder.verify(before).onSwitchToParentFrame(uDriver, uTargetLocator);
		inOrder.verify(uTargetLocator).parentFrame();
		inOrder.verify(after).onSwitchToParentFrame(uDriver, uTargetLocator);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnSwitchToActiveElementListener() {
		WebElement element = mock(WebElement.class);
		when(uTargetLocator.activeElement()).thenReturn(element);
		OnSwitchToActiveElementBeforeEventListener before = mock(OnSwitchToActiveElementBeforeEventListener.class, CALLS_REAL_METHODS);
		OnSwitchToActiveElementAfterEventListener after = mock(OnSwitchToActiveElementAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_SWITCH_TO_ACTIVE_ELEMENT, before);
		setListener(AFTER_SWITCH_TO_ACTIVE_ELEMENT, after);
		InOrder inOrder = inOrder(before, uTargetLocator, after);
		assertThat(((WrapsElement)wTargetLocator.activeElement()).getWrappedElement(), is(equalTo(element)));
		inOrder.verify(before).onSwitchToActiveElement(uDriver, uTargetLocator);
		inOrder.verify(uTargetLocator).activeElement();
		inOrder.verify(after).onSwitchToActiveElement(uDriver, uTargetLocator, element);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnExceptionListener() {
		OnExceptionEventListener listener = mock(OnExceptionEventListener.class, CALLS_REAL_METHODS);
		setListener(ON_EXCEPTION, listener);
		RuntimeException exeption = new RuntimeException("testException");
		doThrow(exeption).when(uTargetLocator).activeElement();
		try {
			wTargetLocator.activeElement();
		} catch (Exception e) {
			assertThat(e, is(equalTo(exeption)));
		}
		InOrder inOrder = inOrder(uTargetLocator, listener);
		inOrder.verify(uTargetLocator).activeElement();
		inOrder.verify(listener).onException(uDriver, uTargetLocator, exeption, "activeElement");
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnSwitchToAlertListener() {
		Alert alert = mock(Alert.class);
		when(uTargetLocator.alert()).thenReturn(alert);
		OnSwitchToAlertBeforeEventListener before = mock(OnSwitchToAlertBeforeEventListener.class, CALLS_REAL_METHODS);
		OnSwitchToAlertAfterEventListener after = mock(OnSwitchToAlertAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_SWITCH_TO_ALERT, before);
		setListener(AFTER_SWITCH_TO_ALERT, after);
		InOrder inOrder = inOrder(before, uTargetLocator, after);
		assertThat(((WrapsAlert)wTargetLocator.alert()).getWrappedAlert(), is(equalTo(alert)));
		inOrder.verify(before).onSwitchToAlert(uDriver, uTargetLocator);
		inOrder.verify(uTargetLocator).alert();
		inOrder.verify(after).onSwitchToAlert(uDriver, uTargetLocator, alert);
		inOrder.verifyNoMoreInteractions();
	}
}
