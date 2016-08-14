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
import static nikoladasm.webdriver.wrapper.internal.TouchScreenWrapperFactory.wrapTouchScreen;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.InOrder;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.internal.Coordinates;

import nikoladasm.webdriver.wrapper.UnderlyingWebDriver;
import nikoladasm.webdriver.wrapper.listeners.OnDoubleTapEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnExceptionEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnLongPressEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSingleTapEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnTouchDownEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnTouchFlickEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnTouchMoveEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnTouchScrollEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnTouchUpEventListener;

public class TouchScreenWrapperFactoryUnitTest extends ListenableBaseTest {

	private final TouchScreen uTouchScreen = mock(TouchScreen.class);
	private final UnderlyingWebDriver uDriver = mock(UnderlyingWebDriver.class);
	
	private TouchScreen wTouchScreen = wrapTouchScreen(uTouchScreen, uDriver, listeners);
	
	@Test
	public void shouldBeReturnWrappedTouchScreen() {
		assertThat(((WrapsTouchScreen) wTouchScreen).getWrappedTouchScreen(), is(equalTo(uTouchScreen)));
	}
	
	@Test
	public void shouldBeFireOnSingleTapListener() {
		OnSingleTapEventListener before = mock(OnSingleTapEventListener.class, CALLS_REAL_METHODS);
		OnSingleTapEventListener after = mock(OnSingleTapEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_SINGLE_TAP, before);
		setListener(AFTER_SINGLE_TAP, after);
		InOrder inOrder = inOrder(before, uTouchScreen, after);
		Coordinates coordinates = mock(Coordinates.class);
		wTouchScreen.singleTap(coordinates);
		inOrder.verify(before).onSingleTap(uDriver, uTouchScreen, coordinates);
		inOrder.verify(uTouchScreen).singleTap(coordinates);
		inOrder.verify(after).onSingleTap(uDriver, uTouchScreen, coordinates);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnExceptionListener() {
		RuntimeException exeption = new RuntimeException("testException");
		Coordinates coordinates = mock(Coordinates.class);
		doThrow(exeption).when(uTouchScreen).singleTap(coordinates);
		OnExceptionEventListener listener = mock(OnExceptionEventListener.class, CALLS_REAL_METHODS);
		setListener(ON_EXCEPTION, listener);
		try {
			wTouchScreen.singleTap(coordinates);
		} catch (Exception e) {
			assertThat(e, is(equalTo(exeption)));
		}
		InOrder inOrder = inOrder(uTouchScreen, listener);
		inOrder.verify(uTouchScreen).singleTap(coordinates);
		inOrder.verify(listener).onException(uDriver, uTouchScreen, exeption, "singleTap");
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnDoubleTapListener() {
		OnDoubleTapEventListener before = mock(OnDoubleTapEventListener.class, CALLS_REAL_METHODS);
		OnDoubleTapEventListener after = mock(OnDoubleTapEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_DOUBLE_TAP, before);
		setListener(AFTER_DOUBLE_TAP, after);
		InOrder inOrder = inOrder(before, uTouchScreen, after);
		Coordinates coordinates = mock(Coordinates.class);
		wTouchScreen.doubleTap(coordinates);
		inOrder.verify(before).onDoubleTap(uDriver, uTouchScreen, coordinates);
		inOrder.verify(uTouchScreen).doubleTap(coordinates);
		inOrder.verify(after).onDoubleTap(uDriver, uTouchScreen, coordinates);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnLongPressListener() {
		OnLongPressEventListener before = mock(OnLongPressEventListener.class, CALLS_REAL_METHODS);
		OnLongPressEventListener after = mock(OnLongPressEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_LONG_PRESS, before);
		setListener(AFTER_LONG_PRESS, after);
		InOrder inOrder = inOrder(before, uTouchScreen, after);
		Coordinates coordinates = mock(Coordinates.class);
		wTouchScreen.longPress(coordinates);
		inOrder.verify(before).onLongPress(uDriver, uTouchScreen, coordinates);
		inOrder.verify(uTouchScreen).longPress(coordinates);
		inOrder.verify(after).onLongPress(uDriver, uTouchScreen, coordinates);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnTouchUpListener() {
		OnTouchUpEventListener before = mock(OnTouchUpEventListener.class, CALLS_REAL_METHODS);
		OnTouchUpEventListener after = mock(OnTouchUpEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_TOUCH_UP, before);
		setListener(AFTER_TOUCH_UP, after);
		InOrder inOrder = inOrder(before, uTouchScreen, after);
		wTouchScreen.up(25, 36);
		inOrder.verify(before).onTouchUp(uDriver, uTouchScreen, 25, 36);
		inOrder.verify(uTouchScreen).up(25, 36);
		inOrder.verify(after).onTouchUp(uDriver, uTouchScreen, 25, 36);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnTouchDownListener() {
		OnTouchDownEventListener before = mock(OnTouchDownEventListener.class, CALLS_REAL_METHODS);
		OnTouchDownEventListener after = mock(OnTouchDownEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_TOUCH_DOWN, before);
		setListener(AFTER_TOUCH_DOWN, after);
		InOrder inOrder = inOrder(before, uTouchScreen, after);
		wTouchScreen.down(25, 36);
		inOrder.verify(before).onTouchDown(uDriver, uTouchScreen, 25, 36);
		inOrder.verify(uTouchScreen).down(25, 36);
		inOrder.verify(after).onTouchDown(uDriver, uTouchScreen, 25, 36);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnTouchMoveListener() {
		OnTouchMoveEventListener before = mock(OnTouchMoveEventListener.class, CALLS_REAL_METHODS);
		OnTouchMoveEventListener after = mock(OnTouchMoveEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_TOUCH_MOVE, before);
		setListener(AFTER_TOUCH_MOVE, after);
		InOrder inOrder = inOrder(before, uTouchScreen, after);
		wTouchScreen.move(25, 36);
		inOrder.verify(before).onTouchMove(uDriver, uTouchScreen, 25, 36);
		inOrder.verify(uTouchScreen).move(25, 36);
		inOrder.verify(after).onTouchMove(uDriver, uTouchScreen, 25, 36);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnTouchScrollListener() {
		OnTouchScrollEventListener before = mock(OnTouchScrollEventListener.class, CALLS_REAL_METHODS);
		OnTouchScrollEventListener after = mock(OnTouchScrollEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_TOUCH_SCROLL, before);
		setListener(AFTER_TOUCH_SCROLL, after);
		InOrder inOrder = inOrder(before, uTouchScreen, after);
		Coordinates coordinates = mock(Coordinates.class);
		wTouchScreen.scroll(23, 44);
		wTouchScreen.scroll(coordinates, 18, 56);
		inOrder.verify(before).onTouchScroll(uDriver, uTouchScreen, null, 23, 44);
		inOrder.verify(uTouchScreen).scroll(23, 44);
		inOrder.verify(after).onTouchScroll(uDriver, uTouchScreen, null, 23, 44);
		inOrder.verify(before).onTouchScroll(uDriver, uTouchScreen, coordinates, 18, 56);
		inOrder.verify(uTouchScreen).scroll(coordinates, 18, 56);
		inOrder.verify(after).onTouchScroll(uDriver, uTouchScreen, coordinates, 18, 56);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnTouchFlickListener() {
		OnTouchFlickEventListener before = mock(OnTouchFlickEventListener.class, CALLS_REAL_METHODS);
		OnTouchFlickEventListener after = mock(OnTouchFlickEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_TOUCH_FLICK, before);
		setListener(AFTER_TOUCH_FLICK, after);
		InOrder inOrder = inOrder(before, uTouchScreen, after);
		Coordinates coordinates = mock(Coordinates.class);
		wTouchScreen.flick(23, 44);
		wTouchScreen.flick(coordinates, 18, 56, 20);
		inOrder.verify(before).onTouchFlick(uDriver, uTouchScreen, null, 0, 0, 0, 23, 44);
		inOrder.verify(uTouchScreen).flick(23, 44);
		inOrder.verify(after).onTouchFlick(uDriver, uTouchScreen, null, 0, 0, 0, 23, 44);
		inOrder.verify(before).onTouchFlick(uDriver, uTouchScreen, coordinates, 18, 56, 20, 0, 0);
		inOrder.verify(uTouchScreen).flick(coordinates, 18, 56, 20);
		inOrder.verify(after).onTouchFlick(uDriver, uTouchScreen, coordinates, 18, 56, 20, 0, 0);
		inOrder.verifyNoMoreInteractions();
	}
}
