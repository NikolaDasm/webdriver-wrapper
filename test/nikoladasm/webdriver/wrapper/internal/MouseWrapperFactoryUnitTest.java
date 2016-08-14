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
import static nikoladasm.webdriver.wrapper.internal.MouseWrapperFactory.wrapMouse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.InOrder;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.internal.Coordinates;

import nikoladasm.webdriver.wrapper.UnderlyingWebDriver;
import nikoladasm.webdriver.wrapper.listeners.OnContextClickEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnDoubleClickEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnExceptionEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnMouseClickEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnMouseDownEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnMouseMoveEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnMouseUpEventListener;

public class MouseWrapperFactoryUnitTest extends ListenableBaseTest {

	private final Mouse uMouse = mock(Mouse.class);
	private final UnderlyingWebDriver uDriver = mock(UnderlyingWebDriver.class);
	
	private Mouse wMouse = wrapMouse(uMouse, uDriver, listeners);
	
	@Test
	public void shouldBeReturnWrappedMouse() {
		assertThat(((WrapsMouse) wMouse).getWrappedMouse(), is(equalTo(uMouse)));
	}
	
	@Test
	public void shouldBeFireOnClickListener() {
		OnMouseClickEventListener before = mock(OnMouseClickEventListener.class, CALLS_REAL_METHODS);
		OnMouseClickEventListener after = mock(OnMouseClickEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_MOUSE_CLICK, before);
		setListener(AFTER_MOUSE_CLICK, after);
		InOrder inOrder = inOrder(before, uMouse, after);
		Coordinates coordinates = mock(Coordinates.class);
		wMouse.click(coordinates);
		inOrder.verify(before).onMouseClick(uDriver, uMouse, coordinates);
		inOrder.verify(uMouse).click(coordinates);
		inOrder.verify(after).onMouseClick(uDriver, uMouse, coordinates);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnExceptionListener() {
		RuntimeException exeption = new RuntimeException("testException");
		Coordinates coordinates = mock(Coordinates.class);
		doThrow(exeption).when(uMouse).click(coordinates);
		OnExceptionEventListener listener = mock(OnExceptionEventListener.class, CALLS_REAL_METHODS);
		setListener(ON_EXCEPTION, listener);
		try {
			wMouse.click(coordinates);
		} catch (Exception e) {
			assertThat(e, is(equalTo(exeption)));
		}
		InOrder inOrder = inOrder(uMouse, listener);
		inOrder.verify(uMouse).click(coordinates);
		inOrder.verify(listener).onException(uDriver, uMouse, exeption, "click");
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnContextClickListener() {
		OnContextClickEventListener before = mock(OnContextClickEventListener.class, CALLS_REAL_METHODS);
		OnContextClickEventListener after = mock(OnContextClickEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_CONTEXT_CLICK, before);
		setListener(AFTER_CONTEXT_CLICK, after);
		InOrder inOrder = inOrder(before, uMouse, after);
		Coordinates coordinates = mock(Coordinates.class);
		wMouse.contextClick(coordinates);
		inOrder.verify(before).onContextClick(uDriver, uMouse, coordinates);
		inOrder.verify(uMouse).contextClick(coordinates);
		inOrder.verify(after).onContextClick(uDriver, uMouse, coordinates);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnDoubleClickListener() {
		OnDoubleClickEventListener before = mock(OnDoubleClickEventListener.class, CALLS_REAL_METHODS);
		OnDoubleClickEventListener after = mock(OnDoubleClickEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_DOUBLE_CLICK, before);
		setListener(AFTER_DOUBLE_CLICK, after);
		InOrder inOrder = inOrder(before, uMouse, after);
		Coordinates coordinates = mock(Coordinates.class);
		wMouse.doubleClick(coordinates);
		inOrder.verify(before).onDoubleClick(uDriver, uMouse, coordinates);
		inOrder.verify(uMouse).doubleClick(coordinates);
		inOrder.verify(after).onDoubleClick(uDriver, uMouse, coordinates);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnMouseDownListener() {
		OnMouseDownEventListener before = mock(OnMouseDownEventListener.class, CALLS_REAL_METHODS);
		OnMouseDownEventListener after = mock(OnMouseDownEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_MOUSE_DOWN, before);
		setListener(AFTER_MOUSE_DOWN, after);
		InOrder inOrder = inOrder(before, uMouse, after);
		Coordinates coordinates = mock(Coordinates.class);
		wMouse.mouseDown(coordinates);
		inOrder.verify(before).onMouseDown(uDriver, uMouse, coordinates);
		inOrder.verify(uMouse).mouseDown(coordinates);
		inOrder.verify(after).onMouseDown(uDriver, uMouse, coordinates);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnMouseUpListener() {
		OnMouseUpEventListener before = mock(OnMouseUpEventListener.class, CALLS_REAL_METHODS);
		OnMouseUpEventListener after = mock(OnMouseUpEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_MOUSE_UP, before);
		setListener(AFTER_MOUSE_UP, after);
		InOrder inOrder = inOrder(before, uMouse, after);
		Coordinates coordinates = mock(Coordinates.class);
		wMouse.mouseUp(coordinates);
		inOrder.verify(before).onMouseUp(uDriver, uMouse, coordinates);
		inOrder.verify(uMouse).mouseUp(coordinates);
		inOrder.verify(after).onMouseUp(uDriver, uMouse, coordinates);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnMouseMoveListener() {
		OnMouseMoveEventListener before = mock(OnMouseMoveEventListener.class, CALLS_REAL_METHODS);
		OnMouseMoveEventListener after = mock(OnMouseMoveEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_MOUSE_MOVE, before);
		setListener(AFTER_MOUSE_MOVE, after);
		InOrder inOrder = inOrder(before, uMouse, after);
		Coordinates coordinates = mock(Coordinates.class);
		wMouse.mouseMove(coordinates);
		wMouse.mouseMove(coordinates, 5, 6);
		inOrder.verify(before).onMouseMove(uDriver, uMouse, coordinates, 0, 0);
		inOrder.verify(uMouse).mouseMove(coordinates);
		inOrder.verify(after).onMouseMove(uDriver, uMouse, coordinates, 0, 0);
		inOrder.verify(before).onMouseMove(uDriver, uMouse, coordinates, 5, 6);
		inOrder.verify(uMouse).mouseMove(coordinates, 5, 6);
		inOrder.verify(after).onMouseMove(uDriver, uMouse, coordinates, 5, 6);
		inOrder.verifyNoMoreInteractions();
	}
}
