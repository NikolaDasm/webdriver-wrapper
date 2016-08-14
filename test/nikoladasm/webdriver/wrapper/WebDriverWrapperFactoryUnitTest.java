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

package nikoladasm.webdriver.wrapper;

import org.junit.Test;
import org.mockito.InOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.internal.WrapsElement;

import nikoladasm.webdriver.wrapper.WebDriverListenableWrapper;
import nikoladasm.webdriver.wrapper.internal.WrapsKeyboard;
import nikoladasm.webdriver.wrapper.internal.WrapsMouse;
import nikoladasm.webdriver.wrapper.internal.WrapsNavigation;
import nikoladasm.webdriver.wrapper.internal.WrapsOptions;
import nikoladasm.webdriver.wrapper.internal.WrapsTargetLocator;
import nikoladasm.webdriver.wrapper.internal.WrapsTouchScreen;
import nikoladasm.webdriver.wrapper.listeners.OnCloseEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnExceptionEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnExecuteAsyncScriptAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnExecuteAsyncScriptBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnExecuteScriptAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnExecuteScriptBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnFindElementAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnFindElementBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnFindElementsAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnFindElementsBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetCurrentUrlAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetCurrentUrlBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetKeyboardAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetKeyboardBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetMouseAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetMouseBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetPageSourceAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetPageSourceBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetScreenshotAsAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetScreenshotAsBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetTitleAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetTitleBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetTouchAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetTouchBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetWindowHandleAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetWindowHandleBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetWindowHandlesAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetWindowHandlesBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnManageAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnManageBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnNavigateAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnNavigateBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnQuitEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSwitchToAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSwitchToBeforeEventListener;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;

import static nikoladasm.webdriver.wrapper.WebDriverWrapperFactory.wrapWebDriver;
import static nikoladasm.webdriver.wrapper.EventListenerLocation.*;

public class WebDriverWrapperFactoryUnitTest {

	private UnderlyingWebDriver uDriver = mock(UnderlyingWebDriver.class);
	private UnderlyingWrappedWebDriver uWrappedDriver = mock(UnderlyingWrappedWebDriver.class);
	private UnderlyingWebElement uElement = mock(UnderlyingWebElement.class);
	private WebDriver wDriver = wrapWebDriver(uDriver);
	
	@Test
	public void shouldBeReturnOriginalWrappedDriver() {
		WebDriver driver = wrapWebDriver(uWrappedDriver);
		when(((WrapsDriver) driver).getWrappedDriver()).thenReturn(uDriver);
		assertThat(((WrapsDriver) driver).getWrappedDriver(), is(equalTo(uDriver)));
	}

	@Test
	public void shouldBeReturnWrappedDriver() {
		assertThat(((WrapsDriver) wDriver).getWrappedDriver(), is(equalTo(uDriver)));
	}
	
	@Test
	public void shouldBeFireOnCloseListeners() {
		OnCloseEventListener before = mock(OnCloseEventListener.class, CALLS_REAL_METHODS);
		OnCloseEventListener after = mock(OnCloseEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_CLOSE, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_CLOSE, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		wDriver.close();
		inOrder.verify(before).onClose(uDriver);
		inOrder.verify(uDriver).close();
		inOrder.verify(after).onClose(uDriver);
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeRemoveListener() {
		OnCloseEventListener before = mock(OnCloseEventListener.class, CALLS_REAL_METHODS);
		OnCloseEventListener after = mock(OnCloseEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_CLOSE, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_CLOSE, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		wDriver.close();
		((WebDriverListenableWrapper) wDriver).removeListener(BEFORE_CLOSE);
		((WebDriverListenableWrapper) wDriver).removeListener(AFTER_CLOSE);
		wDriver.close();
		inOrder.verify(before).onClose(uDriver);
		inOrder.verify(uDriver).close();
		inOrder.verify(after).onClose(uDriver);
		inOrder.verify(uDriver).close();
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnExceptionListener() {
		OnExceptionEventListener listener = mock(OnExceptionEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(ON_EXCEPTION, listener);
		RuntimeException exeption = new RuntimeException("testException");
		doThrow(exeption).when(uDriver).close();
		try {
			wDriver.close();
		} catch (Exception e) {
			assertThat(e, is(equalTo(exeption)));
		}
		InOrder inOrder = inOrder(uDriver, listener);
		inOrder.verify(uDriver).close();
		inOrder.verify(listener).onException(uDriver, null, exeption, "close");
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeRemoveOnExceptionListener() {
		OnExceptionEventListener listener = mock(OnExceptionEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(ON_EXCEPTION, listener);
		RuntimeException exeption = new RuntimeException("testException");
		doThrow(exeption).when(uDriver).close();
		try {
			wDriver.close();
		} catch (Exception e) {
			assertThat(e, is(equalTo(exeption)));
		}
		((WebDriverListenableWrapper) wDriver).removeListener(ON_EXCEPTION);
		try {
			wDriver.close();
		} catch (Exception e) {
			assertThat(e, is(equalTo(exeption)));
		}
		InOrder inOrder = inOrder(uDriver, listener);
		inOrder.verify(uDriver).close();
		inOrder.verify(listener).onException(uDriver, null, exeption, "close");
		inOrder.verify(uDriver).close();
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnQuitListeners() {
		OnQuitEventListener before = mock(OnQuitEventListener.class, CALLS_REAL_METHODS);
		OnQuitEventListener after = mock(OnQuitEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_QUIT, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_QUIT, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		wDriver.quit();
		inOrder.verify(before).onQuit(uDriver);
		inOrder.verify(uDriver).quit();
		inOrder.verify(after).onQuit(uDriver);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetListeners() {
		OnGetEventListener before = mock(OnGetEventListener.class, CALLS_REAL_METHODS);
		OnGetEventListener after = mock(OnGetEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_GET, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_GET, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		wDriver.get("testUrl");
		inOrder.verify(before).onGet(uDriver, "testUrl");
		inOrder.verify(uDriver).get("testUrl");
		inOrder.verify(after).onGet(uDriver, "testUrl");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetCurrentUrlListeners() {
		when(uDriver.getCurrentUrl()).thenReturn("testUrl");
		OnGetCurrentUrlBeforeEventListener before = mock(OnGetCurrentUrlBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetCurrentUrlAfterEventListener after = mock(OnGetCurrentUrlAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_GET_CURRENT_URL, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_GET_CURRENT_URL, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		assertThat(wDriver.getCurrentUrl(), is(equalTo("testUrl")));
		inOrder.verify(before).onGetCurrentUrl(uDriver);
		inOrder.verify(uDriver).getCurrentUrl();
		inOrder.verify(after).onGetCurrentUrl(uDriver, "testUrl");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetPageSourceListeners() {
		when(uDriver.getPageSource()).thenReturn("testSource");
		OnGetPageSourceBeforeEventListener before = mock(OnGetPageSourceBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetPageSourceAfterEventListener after = mock(OnGetPageSourceAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_GET_PAGE_SOURCE, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_GET_PAGE_SOURCE, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		assertThat(wDriver.getPageSource(), is(equalTo("testSource")));
		inOrder.verify(before).onGetPageSource(uDriver);
		inOrder.verify(uDriver).getPageSource();
		inOrder.verify(after).onGetPageSource(uDriver, "testSource");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetTitleListeners() {
		when(uDriver.getTitle()).thenReturn("testTitle");
		OnGetTitleBeforeEventListener before = mock(OnGetTitleBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetTitleAfterEventListener after = mock(OnGetTitleAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_GET_TITLE, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_GET_TITLE, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		assertThat(wDriver.getTitle(), is(equalTo("testTitle")));
		inOrder.verify(before).onGetTitle(uDriver);
		inOrder.verify(uDriver).getTitle();
		inOrder.verify(after).onGetTitle(uDriver, "testTitle");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetWindowHandleListeners() {
		when(uDriver.getWindowHandle()).thenReturn("testHandle");
		OnGetWindowHandleBeforeEventListener before = mock(OnGetWindowHandleBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetWindowHandleAfterEventListener after = mock(OnGetWindowHandleAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_GET_WINDOW_HANDLE, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_GET_WINDOW_HANDLE, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		assertThat(wDriver.getWindowHandle(), is(equalTo("testHandle")));
		inOrder.verify(before).onGetWindowHandle(uDriver);
		inOrder.verify(uDriver).getWindowHandle();
		inOrder.verify(after).onGetWindowHandle(uDriver, "testHandle");
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnGetWindowHandlesListeners() {
		Set<String> handles = new HashSet<>();
		handles.add("testHandle");
		when(uDriver.getWindowHandles()).thenReturn(handles);
		OnGetWindowHandlesBeforeEventListener before = mock(OnGetWindowHandlesBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetWindowHandlesAfterEventListener after = mock(OnGetWindowHandlesAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_GET_WINDOW_HANDLES, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_GET_WINDOW_HANDLES, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		assertThat(wDriver.getWindowHandles(), is(equalTo(handles)));
		inOrder.verify(before).onGetWindowHandles(uDriver);
		inOrder.verify(uDriver).getWindowHandles();
		inOrder.verify(after).onGetWindowHandles(uDriver, handles);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnFindElementListeners() {
		By by = By.id("testId");
		when(uDriver.findElement(by)).thenReturn(uElement);
		OnFindElementBeforeEventListener before = mock(OnFindElementBeforeEventListener.class, CALLS_REAL_METHODS);
		OnFindElementAfterEventListener after = mock(OnFindElementAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_FIND_ELEMENT, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_FIND_ELEMENT, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		WebElement element = wDriver.findElement(by);
		assertThat(((WrapsElement) element).getWrappedElement(), is(equalTo(uElement)));
		assertThat(((WrapsDriver) element).getWrappedDriver(), is(equalTo(uDriver)));
		inOrder.verify(before).onFindElement(uDriver, null, by);
		inOrder.verify(uDriver).findElement(by);
		inOrder.verify(after).onFindElement(uDriver, null, by, uElement);
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnFindElementsListeners() {
		By by = By.id("testId");
		List<WebElement> uElements = new LinkedList<>();
		uElements.add(uElement);
		when(uDriver.findElements(by)).thenReturn(uElements);
		OnFindElementsBeforeEventListener before = mock(OnFindElementsBeforeEventListener.class, CALLS_REAL_METHODS);
		OnFindElementsAfterEventListener after = mock(OnFindElementsAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_FIND_ELEMENTS, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_FIND_ELEMENTS, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		List<WebElement> elements = wDriver.findElements(by);
		assertThat(elements.size(), is(equalTo(uElements.size())));
		assertThat(((WrapsElement) elements.get(0)).getWrappedElement(), is(equalTo(uElement)));
		assertThat(((WrapsDriver) elements.get(0)).getWrappedDriver(), is(equalTo(uDriver)));
		inOrder.verify(before).onFindElements(uDriver, null, by);
		inOrder.verify(uDriver).findElements(by);
		inOrder.verify(after).onFindElements(uDriver, null, by, uElements);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetScreenshotAsListener() {
		when(uDriver.getScreenshotAs(OutputType.BASE64)).thenReturn("testDase64Screenshot");
		OnGetScreenshotAsBeforeEventListener before = mock(OnGetScreenshotAsBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetScreenshotAsAfterEventListener after = mock(OnGetScreenshotAsAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_GET_SCREENSHOT_AS, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_GET_SCREENSHOT_AS, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		assertThat(((TakesScreenshot)wDriver).getScreenshotAs(OutputType.BASE64), is(equalTo("testDase64Screenshot")));
		inOrder.verify(before).onGetScreenshotAs(uDriver, null, OutputType.BASE64);
		inOrder.verify(uDriver).getScreenshotAs(OutputType.BASE64);
		inOrder.verify(after).onGetScreenshotAs(uDriver, null, OutputType.BASE64, "testDase64Screenshot");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnNavigateListeners() {
		Navigation navigation = mock(Navigation.class);
		when(uDriver.navigate()).thenReturn(navigation);
		OnNavigateBeforeEventListener before = mock(OnNavigateBeforeEventListener.class, CALLS_REAL_METHODS);
		OnNavigateAfterEventListener after = mock(OnNavigateAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_NAVIGATE, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_NAVIGATE, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		assertThat(((WrapsNavigation) wDriver.navigate()).getWrappedNavigation(), is(equalTo(navigation)));
		inOrder.verify(before).onNavigate(uDriver);
		inOrder.verify(uDriver).navigate();
		inOrder.verify(after).onNavigate(uDriver, navigation);
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnSwitchToListeners() {
		TargetLocator targetLocator = mock(TargetLocator.class);
		when(uDriver.switchTo()).thenReturn(targetLocator);
		OnSwitchToBeforeEventListener before = mock(OnSwitchToBeforeEventListener.class, CALLS_REAL_METHODS);
		OnSwitchToAfterEventListener after = mock(OnSwitchToAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_SWITCH_TO, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_SWITCH_TO, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		assertThat(((WrapsTargetLocator) wDriver.switchTo()).getWrappedTargetLocator(), is(equalTo(targetLocator)));
		inOrder.verify(before).onSwitchTo(uDriver);
		inOrder.verify(uDriver).switchTo();
		inOrder.verify(after).onSwitchTo(uDriver, targetLocator);
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnManageListeners() {
		Options options = mock(Options.class);
		when(uDriver.manage()).thenReturn(options);
		OnManageBeforeEventListener before = mock(OnManageBeforeEventListener.class, CALLS_REAL_METHODS);
		OnManageAfterEventListener after = mock(OnManageAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_MANAGE, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_MANAGE, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		assertThat(((WrapsOptions) wDriver.manage()).getWrappedOptions(), is(equalTo(options)));
		inOrder.verify(before).onManage(uDriver);
		inOrder.verify(uDriver).manage();
		inOrder.verify(after).onManage(uDriver, options);
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnGetKeyboardListeners() {
		Keyboard keyboard = mock(Keyboard.class);
		when(uDriver.getKeyboard()).thenReturn(keyboard);
		OnGetKeyboardBeforeEventListener before = mock(OnGetKeyboardBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetKeyboardAfterEventListener after = mock(OnGetKeyboardAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_GET_KEYBOARD, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_GET_KEYBOARD, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		assertThat(((WrapsKeyboard) ((HasInputDevices) wDriver).getKeyboard()).getWrappedKeyboard(), is(equalTo(keyboard)));
		inOrder.verify(before).onGetKeyboard(uDriver);
		inOrder.verify(uDriver).getKeyboard();
		inOrder.verify(after).onGetKeyboard(uDriver, keyboard);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetMouseListeners() {
		Mouse mouse = mock(Mouse.class);
		when(uDriver.getMouse()).thenReturn(mouse);
		OnGetMouseBeforeEventListener before = mock(OnGetMouseBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetMouseAfterEventListener after = mock(OnGetMouseAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_GET_MOUSE, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_GET_MOUSE, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		assertThat(((WrapsMouse) ((HasInputDevices) wDriver).getMouse()).getWrappedMouse(), is(equalTo(mouse)));
		inOrder.verify(before).onGetMouse(uDriver);
		inOrder.verify(uDriver).getMouse();
		inOrder.verify(after).onGetMouse(uDriver, mouse);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetTouchListeners() {
		TouchScreen touchScreen = mock(TouchScreen.class);
		when(uDriver.getTouch()).thenReturn(touchScreen);
		OnGetTouchBeforeEventListener before = mock(OnGetTouchBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetTouchAfterEventListener after = mock(OnGetTouchAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_GET_TOUCH, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_GET_TOUCH, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		assertThat(((WrapsTouchScreen) ((HasTouchScreen) wDriver).getTouch()).getWrappedTouchScreen(), is(equalTo(touchScreen)));
		inOrder.verify(before).onGetTouch(uDriver);
		inOrder.verify(uDriver).getTouch();
		inOrder.verify(after).onGetTouch(uDriver, touchScreen);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnExecuteScriptListeners() {
		By by = By.id("testId");
		when(uDriver.findElement(by)).thenReturn(uElement);
		WebElement wElement = wDriver.findElement(by);
		List<WebElement> uElements = new LinkedList<>();
		uElements.add(uElement);
		List<WebElement> wElements = new LinkedList<>();
		wElements.add(wElement);
		Map<WebElement, Object> wsElements = new HashMap<>();
		wsElements.put(wElement, new Object());
		Map<WebElement, Object> usElements = new HashMap<>();
		usElements.put(wElement, uElement);
		when(uDriver.executeScript("testScript", "stringArgs")).thenReturn("testReturnValueForStringArgs");
		when(uDriver.executeScript("testScript", uElement)).thenReturn("testReturnValueForWebElementArgs");
		when(uDriver.executeScript("testScript", uElements)).thenReturn("testReturnValueForWebElementsArgs");
		when(uDriver.executeScript("testScript", usElements)).thenReturn("testReturnValueForMapWebElementsArgs");
		OnExecuteScriptBeforeEventListener before = mock(OnExecuteScriptBeforeEventListener.class, CALLS_REAL_METHODS);
		OnExecuteScriptAfterEventListener after = mock(OnExecuteScriptAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_EXECUTE_SCRIPT, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_EXECUTE_SCRIPT, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		assertThat(((JavascriptExecutor) wDriver).executeScript("testScript", "stringArgs"), is(equalTo("testReturnValueForStringArgs")));
		assertThat(((JavascriptExecutor) wDriver).executeScript("testScript", wElement), is(equalTo("testReturnValueForWebElementArgs")));
		assertThat(((JavascriptExecutor) wDriver).executeScript("testScript", wElements), is(equalTo("testReturnValueForWebElementsArgs")));
		assertThat(((JavascriptExecutor) wDriver).executeScript("testScript", wsElements), is(equalTo("testReturnValueForMapWebElementsArgs")));
		inOrder.verify(before).onExecuteScript(uDriver, "testScript", "stringArgs");
		inOrder.verify(uDriver).executeScript("testScript", "stringArgs");
		inOrder.verify(after).onExecuteScript(uDriver, "testReturnValueForStringArgs", "testScript", "stringArgs");
		inOrder.verify(before).onExecuteScript(uDriver, "testScript", wElement);
		inOrder.verify(uDriver).executeScript("testScript", uElement);
		inOrder.verify(after).onExecuteScript(uDriver, "testReturnValueForWebElementArgs", "testScript", wElement);
		inOrder.verify(before).onExecuteScript(uDriver, "testScript", wElements);
		inOrder.verify(uDriver).executeScript("testScript", uElements);
		inOrder.verify(after).onExecuteScript(uDriver, "testReturnValueForWebElementsArgs", "testScript", wElements);
		inOrder.verify(before).onExecuteScript(uDriver, "testScript", wsElements);
		inOrder.verify(uDriver).executeScript("testScript", usElements);
		inOrder.verify(after).onExecuteScript(uDriver, "testReturnValueForMapWebElementsArgs", "testScript", wsElements);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnExecuteAsyncScriptListeners() {
		By by = By.id("testId");
		when(uDriver.findElement(by)).thenReturn(uElement);
		WebElement wElement = wDriver.findElement(by);
		List<WebElement> uElements = new LinkedList<>();
		uElements.add(uElement);
		List<WebElement> wElements = new LinkedList<>();
		wElements.add(wElement);
		Map<WebElement, Object> wsElements = new HashMap<>();
		wsElements.put(wElement, new Object());
		Map<WebElement, Object> usElements = new HashMap<>();
		usElements.put(wElement, uElement);
		when(uDriver.executeAsyncScript("testScript", "stringArgs")).thenReturn("testReturnValueForStringArgs");
		when(uDriver.executeAsyncScript("testScript", uElement)).thenReturn("testReturnValueForWebElementArgs");
		when(uDriver.executeAsyncScript("testScript", uElements)).thenReturn("testReturnValueForWebElementsArgs");
		when(uDriver.executeAsyncScript("testScript", usElements)).thenReturn("testReturnValueForMapWebElementsArgs");
		OnExecuteAsyncScriptBeforeEventListener before = mock(OnExecuteAsyncScriptBeforeEventListener.class, CALLS_REAL_METHODS);
		OnExecuteAsyncScriptAfterEventListener after = mock(OnExecuteAsyncScriptAfterEventListener.class, CALLS_REAL_METHODS);
		((WebDriverListenableWrapper) wDriver).setListener(BEFORE_EXECUTE_ASYNC_SCRIPT, before);
		((WebDriverListenableWrapper) wDriver).setListener(AFTER_EXECUTE_ASYNC_SCRIPT, after);
		InOrder inOrder = inOrder(before, uDriver, after);
		assertThat(((JavascriptExecutor) wDriver).executeAsyncScript("testScript", "stringArgs"), is(equalTo("testReturnValueForStringArgs")));
		assertThat(((JavascriptExecutor) wDriver).executeAsyncScript("testScript", wElement), is(equalTo("testReturnValueForWebElementArgs")));
		assertThat(((JavascriptExecutor) wDriver).executeAsyncScript("testScript", wElements), is(equalTo("testReturnValueForWebElementsArgs")));
		assertThat(((JavascriptExecutor) wDriver).executeAsyncScript("testScript", wsElements), is(equalTo("testReturnValueForMapWebElementsArgs")));
		inOrder.verify(before).onExecuteAsyncScript(uDriver, "testScript", "stringArgs");
		inOrder.verify(uDriver).executeAsyncScript("testScript", "stringArgs");
		inOrder.verify(after).onExecuteAsyncScript(uDriver, "testReturnValueForStringArgs", "testScript", "stringArgs");
		inOrder.verify(before).onExecuteAsyncScript(uDriver, "testScript", wElement);
		inOrder.verify(uDriver).executeAsyncScript("testScript", uElement);
		inOrder.verify(after).onExecuteAsyncScript(uDriver, "testReturnValueForWebElementArgs", "testScript", wElement);
		inOrder.verify(before).onExecuteAsyncScript(uDriver, "testScript", wElements);
		inOrder.verify(uDriver).executeAsyncScript("testScript", uElements);
		inOrder.verify(after).onExecuteAsyncScript(uDriver, "testReturnValueForWebElementsArgs", "testScript", wElements);
		inOrder.verify(before).onExecuteAsyncScript(uDriver, "testScript", wsElements);
		inOrder.verify(uDriver).executeAsyncScript("testScript", usElements);
		inOrder.verify(after).onExecuteAsyncScript(uDriver, "testReturnValueForMapWebElementsArgs", "testScript", wsElements);
		inOrder.verifyNoMoreInteractions();
	}
}
