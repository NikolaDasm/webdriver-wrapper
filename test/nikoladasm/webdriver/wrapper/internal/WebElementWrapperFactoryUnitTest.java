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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.internal.WrapsElement;

import nikoladasm.webdriver.wrapper.UnderlyingWebDriver;
import nikoladasm.webdriver.wrapper.UnderlyingWebElement;
import nikoladasm.webdriver.wrapper.listeners.OnClearEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnClickEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnExceptionEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnFindElementAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnFindElementBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnFindElementsAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnFindElementsBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetAttributeAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetAttributeBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetCoordinatesAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetCoordinatesBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetCssValueAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetCssValueBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetLocationAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetLocationBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetRectAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetRectBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetScreenshotAsAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetScreenshotAsBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetSizeAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetSizeBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetTagNameAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetTagNameBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetTextAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetTextBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnIsDisplayedAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnIsDisplayedBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnIsEnabledAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnIsEnabledBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnIsSelectedAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnIsSelectedBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSendKeysEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSubmitEventListener;

import static nikoladasm.webdriver.wrapper.internal.WebElementWrapperFactory.wrapWebElement;

public class WebElementWrapperFactoryUnitTest extends ListenableBaseTest {

	private final UnderlyingWebElement uElement = mock(UnderlyingWebElement.class);
	private final UnderlyingWebDriver uDriver = mock(UnderlyingWebDriver.class);
	
	private WebElement wElement = wrapWebElement(uElement, uDriver, listeners);
	
	@Test
	public void shouldBeReturnWrappedDriver() {
		assertThat(((WrapsDriver) wElement).getWrappedDriver(), is(equalTo(uDriver)));
	}
	
	@Test
	public void shouldBeReturnWrappedElement() {
		assertThat(((WrapsElement) wElement).getWrappedElement(), is(equalTo(uElement)));
	}
	
	@Test
	public void shouldBeFireOnClickListener() {
		OnClickEventListener before = mock(OnClickEventListener.class, CALLS_REAL_METHODS);
		OnClickEventListener after = mock(OnClickEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_CLICK, before);
		setListener(AFTER_CLICK, after);
		InOrder inOrder = inOrder(before, uElement, after);
		wElement.click();
		inOrder.verify(before).onClick(uDriver, uElement);
		inOrder.verify(uElement).click();
		inOrder.verify(after).onClick(uDriver, uElement);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnExceptionListener() {
		OnExceptionEventListener listener = mock(OnExceptionEventListener.class, CALLS_REAL_METHODS);
		setListener(ON_EXCEPTION, listener);
		RuntimeException exeption = new RuntimeException("testException");
		doThrow(exeption).when(uElement).click();
		try {
			wElement.click();
		} catch (Exception e) {
			assertThat(e, is(equalTo(exeption)));
		}
		InOrder inOrder = inOrder(uElement, listener);
		inOrder.verify(uElement).click();
		inOrder.verify(listener).onException(uDriver, uElement, exeption, "click");
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnClearListener() {
		OnClearEventListener before = mock(OnClearEventListener.class, CALLS_REAL_METHODS);
		OnClearEventListener after = mock(OnClearEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_CLEAR, before);
		setListener(AFTER_CLEAR, after);
		InOrder inOrder = inOrder(before, uElement, after);
		wElement.clear();
		inOrder.verify(before).onClear(uDriver, uElement);
		inOrder.verify(uElement).clear();
		inOrder.verify(after).onClear(uDriver, uElement);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnSubmitListener() {
		OnSubmitEventListener before = mock(OnSubmitEventListener.class, CALLS_REAL_METHODS);
		OnSubmitEventListener after = mock(OnSubmitEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_SUBMIT, before);
		setListener(AFTER_SUBMIT, after);
		InOrder inOrder = inOrder(before, uElement, after);
		wElement.submit();
		inOrder.verify(before).onSubmit(uDriver, uElement);
		inOrder.verify(uElement).submit();
		inOrder.verify(after).onSubmit(uDriver, uElement);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetTagNameListener() {
		when(uElement.getTagName()).thenReturn("testTagName");
		OnGetTagNameBeforeEventListener before = mock(OnGetTagNameBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetTagNameAfterEventListener after = mock(OnGetTagNameAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_TAG_NAME, before);
		setListener(AFTER_GET_TAG_NAME, after);
		InOrder inOrder = inOrder(before, uElement, after);
		wElement.getTagName();
		inOrder.verify(before).onGetTagName(uDriver, uElement);
		inOrder.verify(uElement).getTagName();
		inOrder.verify(after).onGetTagName(uDriver, uElement, "testTagName");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetTextListener() {
		when(uElement.getText()).thenReturn("testText");
		OnGetTextBeforeEventListener before = mock(OnGetTextBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetTextAfterEventListener after = mock(OnGetTextAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_TEXT, before);
		setListener(AFTER_GET_TEXT, after);
		InOrder inOrder = inOrder(before, uElement, after);
		assertThat(wElement.getText(), is(equalTo("testText")));
		inOrder.verify(before).onGetText(uDriver, uElement);
		inOrder.verify(uElement).getText();
		inOrder.verify(after).onGetText(uDriver, uElement, "testText");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnIsDisplayedListener() {
		when(uElement.isDisplayed()).thenReturn(true);
		OnIsDisplayedBeforeEventListener before = mock(OnIsDisplayedBeforeEventListener.class, CALLS_REAL_METHODS);
		OnIsDisplayedAfterEventListener after = mock(OnIsDisplayedAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_IS_DISPLAYED, before);
		setListener(AFTER_IS_DISPLAYED, after);
		InOrder inOrder = inOrder(before, uElement, after);
		wElement.isDisplayed();
		inOrder.verify(before).onIsDisplayed(uDriver, uElement);
		inOrder.verify(uElement).isDisplayed();
		inOrder.verify(after).onIsDisplayed(uDriver, uElement, true);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnIsEnabledListener() {
		when(uElement.isEnabled()).thenReturn(true);
		OnIsEnabledBeforeEventListener before = mock(OnIsEnabledBeforeEventListener.class, CALLS_REAL_METHODS);
		OnIsEnabledAfterEventListener after = mock(OnIsEnabledAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_IS_ENABLED, before);
		setListener(AFTER_IS_ENABLED, after);
		InOrder inOrder = inOrder(before, uElement, after);
		wElement.isEnabled();
		inOrder.verify(before).onIsEnabled(uDriver, uElement);
		inOrder.verify(uElement).isEnabled();
		inOrder.verify(after).onIsEnabled(uDriver, uElement, true);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnIsSelectedListener() {
		when(uElement.isSelected()).thenReturn(true);
		OnIsSelectedBeforeEventListener before = mock(OnIsSelectedBeforeEventListener.class, CALLS_REAL_METHODS);
		OnIsSelectedAfterEventListener after = mock(OnIsSelectedAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_IS_SELECTED, before);
		setListener(AFTER_IS_SELECTED, after);
		InOrder inOrder = inOrder(before, uElement, after);
		wElement.isSelected();
		inOrder.verify(before).onIsSelected(uDriver, uElement);
		inOrder.verify(uElement).isSelected();
		inOrder.verify(after).onIsSelected(uDriver, uElement, true);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnSendKeysListener() {
		OnSendKeysEventListener before = mock(OnSendKeysEventListener.class, CALLS_REAL_METHODS);
		OnSendKeysEventListener after = mock(OnSendKeysEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_SEND_KEYS, before);
		setListener(AFTER_SEND_KEYS, after);
		InOrder inOrder = inOrder(before, uElement, after);
		wElement.sendKeys("testKeys");
		inOrder.verify(before).onSendKeys(uDriver, uElement, "testKeys");
		inOrder.verify(uElement).sendKeys("testKeys");;
		inOrder.verify(after).onSendKeys(uDriver, uElement, "testKeys");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetAttributeListener() {
		when(uElement.getAttribute("testName")).thenReturn("testValue");
		OnGetAttributeBeforeEventListener before = mock(OnGetAttributeBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetAttributeAfterEventListener after = mock(OnGetAttributeAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_ATTRIBUTE, before);
		setListener(AFTER_GET_ATTRIBUTE, after);
		InOrder inOrder = inOrder(before, uElement, after);
		assertThat(wElement.getAttribute("testName"), is(equalTo("testValue")));
		inOrder.verify(before).onGetAttribute(uDriver, uElement, "testName");
		inOrder.verify(uElement).getAttribute("testName");
		inOrder.verify(after).onGetAttribute(uDriver, uElement, "testName", "testValue");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetCssValueListener() {
		when(uElement.getCssValue("testPropertyName")).thenReturn("testValue");
		OnGetCssValueBeforeEventListener before = mock(OnGetCssValueBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetCssValueAfterEventListener after = mock(OnGetCssValueAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_CSS_VALUE, before);
		setListener(AFTER_GET_CSS_VALUE, after);
		InOrder inOrder = inOrder(before, uElement, after);
		assertThat(wElement.getCssValue("testPropertyName"), is(equalTo("testValue")));
		inOrder.verify(before).onGetCssValue(uDriver, uElement, "testPropertyName");
		inOrder.verify(uElement).getCssValue("testPropertyName");
		inOrder.verify(after).onGetCssValue(uDriver, uElement, "testPropertyName", "testValue");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetLocationListener() {
		Point point = mock(Point.class);
		when(uElement.getLocation()).thenReturn(point);
		OnGetLocationBeforeEventListener before = mock(OnGetLocationBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetLocationAfterEventListener after = mock(OnGetLocationAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_LOCATION, before);
		setListener(AFTER_GET_LOCATION, after);
		InOrder inOrder = inOrder(before, uElement, after);
		assertThat(wElement.getLocation(), is(equalTo(point)));
		inOrder.verify(before).onGetLocation(uDriver, uElement);
		inOrder.verify(uElement).getLocation();
		inOrder.verify(after).onGetLocation(uDriver, uElement, point);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetRectListener() {
		Rectangle rect = mock(Rectangle.class);
		when(uElement.getRect()).thenReturn(rect);
		OnGetRectBeforeEventListener before = mock(OnGetRectBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetRectAfterEventListener after = mock(OnGetRectAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_RECT, before);
		setListener(AFTER_GET_RECT, after);
		InOrder inOrder = inOrder(before, uElement, after);
		assertThat(wElement.getRect(), is(equalTo(rect)));
		inOrder.verify(before).onGetRect(uDriver, uElement);
		inOrder.verify(uElement).getRect();
		inOrder.verify(after).onGetRect(uDriver, uElement, rect);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetSizeListener() {
		Dimension size = mock(Dimension.class);
		when(uElement.getSize()).thenReturn(size);
		OnGetSizeBeforeEventListener before = mock(OnGetSizeBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetSizeAfterEventListener after = mock(OnGetSizeAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_SIZE, before);
		setListener(AFTER_GET_SIZE, after);
		InOrder inOrder = inOrder(before, uElement, after);
		assertThat(wElement.getSize(), is(equalTo(size)));
		inOrder.verify(before).onGetSize(uDriver, uElement);
		inOrder.verify(uElement).getSize();
		inOrder.verify(after).onGetSize(uDriver, uElement, size);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnFindElementListeners() {
		By by = By.id("testId");
		WebElement fElement = mock(WebElement.class);
		when(uElement.findElement(by)).thenReturn(fElement);
		OnFindElementBeforeEventListener before = mock(OnFindElementBeforeEventListener.class, CALLS_REAL_METHODS);
		OnFindElementAfterEventListener after = mock(OnFindElementAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_FIND_ELEMENT, before);
		setListener(AFTER_FIND_ELEMENT, after);
		InOrder inOrder = inOrder(before, uElement, after);
		WebElement element = wElement.findElement(by);
		assertThat(((WrapsElement) element).getWrappedElement(), is(equalTo(fElement)));
		assertThat(((WrapsDriver) element).getWrappedDriver(), is(equalTo(uDriver)));
		inOrder.verify(before).onFindElement(uDriver, uElement, by);
		inOrder.verify(uElement).findElement(by);
		inOrder.verify(after).onFindElement(uDriver, uElement, by, fElement);
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnFindElementsListeners() {
		By by = By.id("testId");
		WebElement fElement = mock(WebElement.class);
		List<WebElement> uElements = new LinkedList<>();
		uElements.add(fElement);
		when(uElement.findElements(by)).thenReturn(uElements);
		OnFindElementsBeforeEventListener before = mock(OnFindElementsBeforeEventListener.class, CALLS_REAL_METHODS);
		OnFindElementsAfterEventListener after = mock(OnFindElementsAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_FIND_ELEMENTS, before);
		setListener(AFTER_FIND_ELEMENTS, after);
		InOrder inOrder = inOrder(before, uElement, after);
		List<WebElement> elements = wElement.findElements(by);
		assertThat(elements.size(), is(equalTo(uElements.size())));
		assertThat(((WrapsElement) elements.get(0)).getWrappedElement(), is(equalTo(fElement)));
		assertThat(((WrapsDriver) elements.get(0)).getWrappedDriver(), is(equalTo(uDriver)));
		inOrder.verify(before).onFindElements(uDriver, uElement, by);
		inOrder.verify(uElement).findElements(by);
		inOrder.verify(after).onFindElements(uDriver, uElement, by, uElements);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetScreenshotAsListener() {
		when(uElement.getScreenshotAs(OutputType.BASE64)).thenReturn("testDase64Screenshot");
		OnGetScreenshotAsBeforeEventListener before = mock(OnGetScreenshotAsBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetScreenshotAsAfterEventListener after = mock(OnGetScreenshotAsAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_SCREENSHOT_AS, before);
		setListener(AFTER_GET_SCREENSHOT_AS, after);
		InOrder inOrder = inOrder(before, uElement, after);
		assertThat(wElement.getScreenshotAs(OutputType.BASE64), is(equalTo("testDase64Screenshot")));
		inOrder.verify(before).onGetScreenshotAs(uDriver, uElement, OutputType.BASE64);
		inOrder.verify(uElement).getScreenshotAs(OutputType.BASE64);
		inOrder.verify(after).onGetScreenshotAs(uDriver, uElement, OutputType.BASE64, "testDase64Screenshot");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetCoordinatesListener() {
		Coordinates coordinates = mock(Coordinates.class);
		when(uElement.getCoordinates()).thenReturn(coordinates);
		OnGetCoordinatesBeforeEventListener before = mock(OnGetCoordinatesBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetCoordinatesAfterEventListener after = mock(OnGetCoordinatesAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_COORDINATES, before);
		setListener(AFTER_GET_COORDINATES, after);
		InOrder inOrder = inOrder(before, uElement, after);
		assertThat(((Locatable)wElement).getCoordinates(), is(equalTo(coordinates)));
		inOrder.verify(before).onGetCoordinates(uDriver, uElement);
		inOrder.verify(uElement).getCoordinates();
		inOrder.verify(after).onGetCoordinates(uDriver, uElement, coordinates);
		inOrder.verifyNoMoreInteractions();
	}
}
