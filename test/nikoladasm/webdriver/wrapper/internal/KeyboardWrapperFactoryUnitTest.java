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
import static nikoladasm.webdriver.wrapper.internal.KeyboardWrapperFactory.wrapKeyboard;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.InOrder;
import org.openqa.selenium.interactions.Keyboard;

import nikoladasm.webdriver.wrapper.UnderlyingWebDriver;
import nikoladasm.webdriver.wrapper.listeners.OnExceptionEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnPressKeyEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnReleaseKeyEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSendKeysFromKeyboardEventListener;

public class KeyboardWrapperFactoryUnitTest extends ListenableBaseTest {

	private final Keyboard uKeyboard = mock(Keyboard.class);
	private final UnderlyingWebDriver uDriver = mock(UnderlyingWebDriver.class);
	
	private Keyboard wKeyboard = wrapKeyboard(uKeyboard, uDriver, listeners);
	
	@Test
	public void shouldBeReturnWrappedKeyboard() {
		assertThat(((WrapsKeyboard) wKeyboard).getWrappedKeyboard(), is(equalTo(uKeyboard)));
	}
	
	@Test
	public void shouldBeFireOnPressKeyListener() {
		OnPressKeyEventListener before = mock(OnPressKeyEventListener.class, CALLS_REAL_METHODS);
		OnPressKeyEventListener after = mock(OnPressKeyEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_PRESS_KEY, before);
		setListener(AFTER_PRESS_KEY, after);
		InOrder inOrder = inOrder(before, uKeyboard, after);
		wKeyboard.pressKey("testKey");
		inOrder.verify(before).onPressKey(uDriver, uKeyboard, "testKey");
		inOrder.verify(uKeyboard).pressKey("testKey");
		inOrder.verify(after).onPressKey(uDriver, uKeyboard, "testKey");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnExceptionListener() {
		RuntimeException exeption = new RuntimeException("testException");
		doThrow(exeption).when(uKeyboard).pressKey("testKey");
		OnExceptionEventListener listener = mock(OnExceptionEventListener.class, CALLS_REAL_METHODS);
		setListener(ON_EXCEPTION, listener);
		try {
			wKeyboard.pressKey("testKey");
		} catch (Exception e) {
			assertThat(e, is(equalTo(exeption)));
		}
		InOrder inOrder = inOrder(uKeyboard, listener);
		inOrder.verify(uKeyboard).pressKey("testKey");
		inOrder.verify(listener).onException(uDriver, uKeyboard, exeption, "pressKey");
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnReleaseKeyListener() {
		OnReleaseKeyEventListener before = mock(OnReleaseKeyEventListener.class, CALLS_REAL_METHODS);
		OnReleaseKeyEventListener after = mock(OnReleaseKeyEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_RELEASE_KEY, before);
		setListener(AFTER_RELEASE_KEY, after);
		InOrder inOrder = inOrder(before, uKeyboard, after);
		wKeyboard.releaseKey("testKey");
		inOrder.verify(before).onReleaseKey(uDriver, uKeyboard, "testKey");
		inOrder.verify(uKeyboard).releaseKey("testKey");
		inOrder.verify(after).onReleaseKey(uDriver, uKeyboard, "testKey");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnSendKeysListener() {
		OnSendKeysFromKeyboardEventListener before = mock(OnSendKeysFromKeyboardEventListener.class, CALLS_REAL_METHODS);
		OnSendKeysFromKeyboardEventListener after = mock(OnSendKeysFromKeyboardEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_SEND_KEYS_FROM_KEYBOARD, before);
		setListener(AFTER_SEND_KEYS_FROM_KEYBOARD, after);
		InOrder inOrder = inOrder(before, uKeyboard, after);
		wKeyboard.sendKeys("testKey");
		inOrder.verify(before).onSendKeys(uDriver, uKeyboard, "testKey");
		inOrder.verify(uKeyboard).sendKeys("testKey");
		inOrder.verify(after).onSendKeys(uDriver, uKeyboard, "testKey");
		inOrder.verifyNoMoreInteractions();
	}
}
