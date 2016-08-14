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
import static nikoladasm.webdriver.wrapper.internal.WindowWrapperFactory.wrapWindow;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.InOrder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver.Window;

import nikoladasm.webdriver.wrapper.UnderlyingWebDriver;
import nikoladasm.webdriver.wrapper.listeners.OnExceptionEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnFullscreenEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetPositionAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetPositionBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetWindowSizeAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetWindowSizeBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnMaximizeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSetPositionEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSetWindowSizeEventListener;

public class WindowWrapperFactoryUnitTest extends ListenableBaseTest {

	private final Window uWindow = mock(Window.class);
	private final UnderlyingWebDriver uDriver = mock(UnderlyingWebDriver.class);
	
	private Window wWindow = wrapWindow(uWindow, uDriver, listeners);
	
	@Test
	public void shouldBeReturnWrappedWindow() {
		assertThat(((WrapsWindow) wWindow).getWrappedWindow(), is(equalTo(uWindow)));
	}

	@Test
	public void shouldBeFireOnFullscreenListener() {
		OnFullscreenEventListener before = mock(OnFullscreenEventListener.class, CALLS_REAL_METHODS);
		OnFullscreenEventListener after = mock(OnFullscreenEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_FULLSCREEN, before);
		setListener(AFTER_FULLSCREEN, after);
		InOrder inOrder = inOrder(before, uWindow, after);
		wWindow.fullscreen();
		inOrder.verify(before).onFullscreen(uDriver, uWindow);
		inOrder.verify(uWindow).fullscreen();
		inOrder.verify(after).onFullscreen(uDriver, uWindow);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnExceptionListener() {
		OnExceptionEventListener listener = mock(OnExceptionEventListener.class, CALLS_REAL_METHODS);
		setListener(ON_EXCEPTION, listener);
		RuntimeException exeption = new RuntimeException("testException");
		doThrow(exeption).when(uWindow).fullscreen();
		try {
			wWindow.fullscreen();
		} catch (Exception e) {
			assertThat(e, is(equalTo(exeption)));
		}
		InOrder inOrder = inOrder(uWindow, listener);
		inOrder.verify(uWindow).fullscreen();
		inOrder.verify(listener).onException(uDriver, uWindow, exeption, "fullscreen");
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnMaximizeListener() {
		OnMaximizeEventListener before = mock(OnMaximizeEventListener.class, CALLS_REAL_METHODS);
		OnMaximizeEventListener after = mock(OnMaximizeEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_MAXIMIZE, before);
		setListener(AFTER_MAXIMIZE, after);
		InOrder inOrder = inOrder(before, uWindow, after);
		wWindow.maximize();
		inOrder.verify(before).onMaximize(uDriver, uWindow);
		inOrder.verify(uWindow).maximize();
		inOrder.verify(after).onMaximize(uDriver, uWindow);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetPositionListener() {
		Point point = mock(Point.class);
		when(uWindow.getPosition()).thenReturn(point);
		OnGetPositionBeforeEventListener before = mock(OnGetPositionBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetPositionAfterEventListener after = mock(OnGetPositionAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_POSITION, before);
		setListener(AFTER_GET_POSITION, after);
		InOrder inOrder = inOrder(before, uWindow, after);
		assertThat(wWindow.getPosition(), is(equalTo(point)));
		inOrder.verify(before).onGetPosition(uDriver, uWindow);
		inOrder.verify(uWindow).getPosition();
		inOrder.verify(after).onGetPosition(uDriver, uWindow, point);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetSizeListener() {
		Dimension dimension = mock(Dimension.class);
		when(uWindow.getSize()).thenReturn(dimension);
		OnGetWindowSizeBeforeEventListener before = mock(OnGetWindowSizeBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetWindowSizeAfterEventListener after = mock(OnGetWindowSizeAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_WINDOW_SIZE, before);
		setListener(AFTER_GET_WINDOW_SIZE, after);
		InOrder inOrder = inOrder(before, uWindow, after);
		assertThat(wWindow.getSize(), is(equalTo(dimension)));
		inOrder.verify(before).onGetSize(uDriver, uWindow);
		inOrder.verify(uWindow).getSize();
		inOrder.verify(after).onGetSize(uDriver, uWindow, dimension);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnSetPositionListener() {
		OnSetPositionEventListener before = mock(OnSetPositionEventListener.class, CALLS_REAL_METHODS);
		OnSetPositionEventListener after = mock(OnSetPositionEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_SET_POSITION, before);
		setListener(AFTER_SET_POSITION, after);
		InOrder inOrder = inOrder(before, uWindow, after);
		Point point = mock(Point.class);
		wWindow.setPosition(point);
		inOrder.verify(before).onSetPosition(uDriver, uWindow, point);
		inOrder.verify(uWindow).setPosition(point);
		inOrder.verify(after).onSetPosition(uDriver, uWindow, point);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnSetSizeListener() {
		OnSetWindowSizeEventListener before = mock(OnSetWindowSizeEventListener.class, CALLS_REAL_METHODS);
		OnSetWindowSizeEventListener after = mock(OnSetWindowSizeEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_SET_WINDOW_SIZE, before);
		setListener(AFTER_SET_WINDOW_SIZE, after);
		InOrder inOrder = inOrder(before, uWindow, after);
		Dimension dimension = mock(Dimension.class);
		wWindow.setSize(dimension);
		inOrder.verify(before).onSetSize(uDriver, uWindow, dimension);
		inOrder.verify(uWindow).setSize(dimension);
		inOrder.verify(after).onSetSize(uDriver, uWindow, dimension);
		inOrder.verifyNoMoreInteractions();
	}
}
