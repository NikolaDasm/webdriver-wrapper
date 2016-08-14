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
import static nikoladasm.webdriver.wrapper.internal.TimeoutsWrapperFactory.wrapTimeouts;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.mockito.InOrder;
import org.openqa.selenium.WebDriver.Timeouts;

import nikoladasm.webdriver.wrapper.UnderlyingWebDriver;
import nikoladasm.webdriver.wrapper.listeners.OnExceptionEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnImplicitlyWaitEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnPageLoadTimeoutEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSetScriptTimeoutEventListener;

public class TimeoutsWrapperFactoryUnitTest extends ListenableBaseTest {

	private final Timeouts uTimeouts = mock(Timeouts.class);
	private final UnderlyingWebDriver uDriver = mock(UnderlyingWebDriver.class);
	
	private Timeouts wTimeouts = wrapTimeouts(uTimeouts, uDriver, listeners);
	
	@Test
	public void shouldBeReturnWrappedTimeouts() {
		assertThat(((WrapsTimeouts) wTimeouts).getWrappedTimeouts(), is(equalTo(uTimeouts)));
	}
	
	@Test
	public void shouldBeFireOnImplicitlyWaitListener() {
		when(uTimeouts.implicitlyWait(100, TimeUnit.MILLISECONDS)).thenReturn(uTimeouts);
		OnImplicitlyWaitEventListener before = mock(OnImplicitlyWaitEventListener.class, CALLS_REAL_METHODS);
		OnImplicitlyWaitEventListener after = mock(OnImplicitlyWaitEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_IMPLICITLY_WAIT, before);
		setListener(AFTER_IMPLICITLY_WAIT, after);
		InOrder inOrder = inOrder(before, uTimeouts, after);
		assertThat(wTimeouts.implicitlyWait(100, TimeUnit.MILLISECONDS), is(equalTo(wTimeouts)));
		inOrder.verify(before).onImplicitlyWait(uDriver, uTimeouts, 100, TimeUnit.MILLISECONDS);
		inOrder.verify(uTimeouts).implicitlyWait(100, TimeUnit.MILLISECONDS);
		inOrder.verify(after).onImplicitlyWait(uDriver, uTimeouts, 100, TimeUnit.MILLISECONDS);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnExceptionListener() {
		RuntimeException exeption = new RuntimeException("testException");
		doThrow(exeption).when(uTimeouts).implicitlyWait(100, TimeUnit.MILLISECONDS);
		OnExceptionEventListener listener = mock(OnExceptionEventListener.class, CALLS_REAL_METHODS);
		setListener(ON_EXCEPTION, listener);
		try {
			wTimeouts.implicitlyWait(100, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			assertThat(e, is(equalTo(exeption)));
		}
		InOrder inOrder = inOrder(uTimeouts, listener);
		inOrder.verify(uTimeouts).implicitlyWait(100, TimeUnit.MILLISECONDS);
		inOrder.verify(listener).onException(uDriver, uTimeouts, exeption, "implicitlyWait");
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnSetScriptTimeoutListener() {
		when(uTimeouts.setScriptTimeout(100, TimeUnit.MILLISECONDS)).thenReturn(uTimeouts);
		OnSetScriptTimeoutEventListener before = mock(OnSetScriptTimeoutEventListener.class, CALLS_REAL_METHODS);
		OnSetScriptTimeoutEventListener after = mock(OnSetScriptTimeoutEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_SET_SCRIPT_TIMEOUT, before);
		setListener(AFTER_SET_SCRIPT_TIMEOUT, after);
		InOrder inOrder = inOrder(before, uTimeouts, after);
		assertThat(wTimeouts.setScriptTimeout(100, TimeUnit.MILLISECONDS), is(equalTo(wTimeouts)));
		inOrder.verify(before).onSetScriptTimeout(uDriver, uTimeouts, 100, TimeUnit.MILLISECONDS);
		inOrder.verify(uTimeouts).setScriptTimeout(100, TimeUnit.MILLISECONDS);
		inOrder.verify(after).onSetScriptTimeout(uDriver, uTimeouts, 100, TimeUnit.MILLISECONDS);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnPageLoadTimeoutListener() {
		when(uTimeouts.pageLoadTimeout(100, TimeUnit.MILLISECONDS)).thenReturn(uTimeouts);
		OnPageLoadTimeoutEventListener before = mock(OnPageLoadTimeoutEventListener.class, CALLS_REAL_METHODS);
		OnPageLoadTimeoutEventListener after = mock(OnPageLoadTimeoutEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_PAGE_LOAD_TIMEOUT, before);
		setListener(AFTER_PAGE_LOAD_TIMEOUT, after);
		InOrder inOrder = inOrder(before, uTimeouts, after);
		assertThat(wTimeouts.pageLoadTimeout(100, TimeUnit.MILLISECONDS), is(equalTo(wTimeouts)));
		inOrder.verify(before).onPageLoadTimeout(uDriver, uTimeouts, 100, TimeUnit.MILLISECONDS);
		inOrder.verify(uTimeouts).pageLoadTimeout(100, TimeUnit.MILLISECONDS);
		inOrder.verify(after).onPageLoadTimeout(uDriver, uTimeouts, 100, TimeUnit.MILLISECONDS);
		inOrder.verifyNoMoreInteractions();
	}
}
