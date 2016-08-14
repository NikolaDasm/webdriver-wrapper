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
import static nikoladasm.webdriver.wrapper.internal.OptionsWrapperFactory.wrapOptions;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.InOrder;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver.ImeHandler;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.logging.Logs;

import nikoladasm.webdriver.wrapper.UnderlyingWebDriver;
import nikoladasm.webdriver.wrapper.listeners.OnAddCookieEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnDeleteAllCookiesEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnDeleteCookieEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnDeleteCookieNamedEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnExceptionEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetCookieNamedAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetCookieNamedBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetCookiesAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetCookiesBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnImeAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnImeBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnLogsAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnLogsBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnTimeoutsAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnTimeoutsBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnWindowAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnWindowBeforeEventListener;

public class OptionsWrapperFactoryUnitTest extends ListenableBaseTest {

	private final Options uOptions = mock(Options.class);
	private final UnderlyingWebDriver uDriver = mock(UnderlyingWebDriver.class);
	
	private Options wOptions = wrapOptions(uOptions, uDriver, listeners);
	
	@Test
	public void shouldBeReturnWrappedOptions() {
		assertThat(((WrapsOptions) wOptions).getWrappedOptions(), is(equalTo(uOptions)));
	}
	
	@Test
	public void shouldBeFireOnAddCookieListener() {
		Cookie cookie = mock(Cookie.class);
		OnAddCookieEventListener before = mock(OnAddCookieEventListener.class, CALLS_REAL_METHODS);
		OnAddCookieEventListener after = mock(OnAddCookieEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_ADD_COOKIE, before);
		setListener(AFTER_ADD_COOKIE, after);
		InOrder inOrder = inOrder(before, uOptions, after);
		wOptions.addCookie(cookie);
		inOrder.verify(before).onAddCookie(uDriver, uOptions, cookie);
		inOrder.verify(uOptions).addCookie(cookie);
		inOrder.verify(after).onAddCookie(uDriver, uOptions, cookie);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnExceptionListener() {
		Cookie cookie = mock(Cookie.class);
		RuntimeException exeption = new RuntimeException("testException");
		doThrow(exeption).when(uOptions).addCookie(cookie);
		OnExceptionEventListener listener = mock(OnExceptionEventListener.class, CALLS_REAL_METHODS);
		setListener(ON_EXCEPTION, listener);
		try {
			wOptions.addCookie(cookie);
		} catch (Exception e) {
			assertThat(e, is(equalTo(exeption)));
		}
		InOrder inOrder = inOrder(uOptions, listener);
		inOrder.verify(uOptions).addCookie(cookie);
		inOrder.verify(listener).onException(uDriver, uOptions, exeption, "addCookie");
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnDeleteCookieListener() {
		Cookie cookie = mock(Cookie.class);
		OnDeleteCookieEventListener before = mock(OnDeleteCookieEventListener.class, CALLS_REAL_METHODS);
		OnDeleteCookieEventListener after = mock(OnDeleteCookieEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_DELETE_COOKIE, before);
		setListener(AFTER_DELETE_COOKIE, after);
		InOrder inOrder = inOrder(before, uOptions, after);
		wOptions.deleteCookie(cookie);
		inOrder.verify(before).onDeleteCookie(uDriver, uOptions, cookie);
		inOrder.verify(uOptions).deleteCookie(cookie);
		inOrder.verify(after).onDeleteCookie(uDriver, uOptions, cookie);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnDeleteAllCookiesListener() {
		OnDeleteAllCookiesEventListener before = mock(OnDeleteAllCookiesEventListener.class, CALLS_REAL_METHODS);
		OnDeleteAllCookiesEventListener after = mock(OnDeleteAllCookiesEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_DELETE_ALL_COOKIES, before);
		setListener(AFTER_DELETE_ALL_COOKIES, after);
		InOrder inOrder = inOrder(before, uOptions, after);
		wOptions.deleteAllCookies();
		inOrder.verify(before).onDeleteAllCookies(uDriver, uOptions);
		inOrder.verify(uOptions).deleteAllCookies();
		inOrder.verify(after).onDeleteAllCookies(uDriver, uOptions);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetCookieNamedListener() {
		Cookie cookie = mock(Cookie.class);
		when(uOptions.getCookieNamed("testCookieName")).thenReturn(cookie);
		OnGetCookieNamedBeforeEventListener before = mock(OnGetCookieNamedBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetCookieNamedAfterEventListener after = mock(OnGetCookieNamedAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_COOKIE_NAMED, before);
		setListener(AFTER_GET_COOKIE_NAMED, after);
		InOrder inOrder = inOrder(before, uOptions, after);
		assertThat(wOptions.getCookieNamed("testCookieName"), is(equalTo(cookie)));
		inOrder.verify(before).onGetCookieNamed(uDriver, uOptions, "testCookieName");
		inOrder.verify(uOptions).getCookieNamed("testCookieName");
		inOrder.verify(after).onGetCookieNamed(uDriver, uOptions, "testCookieName", cookie);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnDeleteCookieNamedListener() {
		OnDeleteCookieNamedEventListener before = mock(OnDeleteCookieNamedEventListener.class, CALLS_REAL_METHODS);
		OnDeleteCookieNamedEventListener after = mock(OnDeleteCookieNamedEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_DELETE_COOKIE_NAMED, before);
		setListener(AFTER_DELETE_COOKIE_NAMED, after);
		InOrder inOrder = inOrder(before, uOptions, after);
		wOptions.deleteCookieNamed("testCookieName");
		inOrder.verify(before).onDeleteCookieNamed(uDriver, uOptions, "testCookieName");
		inOrder.verify(uOptions).deleteCookieNamed("testCookieName");
		inOrder.verify(after).onDeleteCookieNamed(uDriver, uOptions, "testCookieName");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetCookiesListener() {
		Cookie cookie = mock(Cookie.class);
		Set<Cookie> fCookies = new HashSet<>();
		fCookies.add(cookie);
		when(uOptions.getCookies()).thenReturn(fCookies);
		OnGetCookiesBeforeEventListener before = mock(OnGetCookiesBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetCookiesAfterEventListener after = mock(OnGetCookiesAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_COOKIES, before);
		setListener(AFTER_GET_COOKIES, after);
		InOrder inOrder = inOrder(before, uOptions, after);
		Set<Cookie> cookies = wOptions.getCookies();
		assertThat(cookies.size(), is(equalTo(fCookies.size())));
		assertThat(cookies.contains(cookie), is(equalTo(true)));
		inOrder.verify(before).onGetCookies(uDriver, uOptions);
		inOrder.verify(uOptions).getCookies();
		inOrder.verify(after).onGetCookies(uDriver, uOptions, fCookies);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnLogsListener() {
		Logs logs = mock(Logs.class);
		when(uOptions.logs()).thenReturn(logs);
		OnLogsBeforeEventListener before = mock(OnLogsBeforeEventListener.class, CALLS_REAL_METHODS);
		OnLogsAfterEventListener after = mock(OnLogsAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_LOGS, before);
		setListener(AFTER_LOGS, after);
		InOrder inOrder = inOrder(before, uOptions, after);
		assertThat(((WrapsLogs) wOptions.logs()).getWrappedLogs(), is(equalTo(logs)));
		inOrder.verify(before).onLogs(uDriver, uOptions);
		inOrder.verify(uOptions).logs();
		inOrder.verify(after).onLogs(uDriver, uOptions, logs);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnImeListener() {
		ImeHandler imeHandler = mock(ImeHandler.class);
		when(uOptions.ime()).thenReturn(imeHandler);
		OnImeBeforeEventListener before = mock(OnImeBeforeEventListener.class, CALLS_REAL_METHODS);
		OnImeAfterEventListener after = mock(OnImeAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_IME, before);
		setListener(AFTER_IME, after);
		InOrder inOrder = inOrder(before, uOptions, after);
		assertThat(((WrapsImeHandler) wOptions.ime()).getWrappedImeHandler(), is(equalTo(imeHandler)));
		inOrder.verify(before).onIme(uDriver, uOptions);
		inOrder.verify(uOptions).ime();
		inOrder.verify(after).onIme(uDriver, uOptions, imeHandler);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnWindowListener() {
		Window window = mock(Window.class);
		when(uOptions.window()).thenReturn(window);
		OnWindowBeforeEventListener before = mock(OnWindowBeforeEventListener.class, CALLS_REAL_METHODS);
		OnWindowAfterEventListener after = mock(OnWindowAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_WINDOW, before);
		setListener(AFTER_WINDOW, after);
		InOrder inOrder = inOrder(before, uOptions, after);
		assertThat(((WrapsWindow) wOptions.window()).getWrappedWindow(), is(equalTo(window)));
		inOrder.verify(before).onWindow(uDriver, uOptions);
		inOrder.verify(uOptions).window();
		inOrder.verify(after).onWindow(uDriver, uOptions, window);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnTimeoutsListener() {
		Timeouts timeouts = mock(Timeouts.class);
		when(uOptions.timeouts()).thenReturn(timeouts);
		OnTimeoutsBeforeEventListener before = mock(OnTimeoutsBeforeEventListener.class, CALLS_REAL_METHODS);
		OnTimeoutsAfterEventListener after = mock(OnTimeoutsAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_TIMEOUTS, before);
		setListener(AFTER_TIMEOUTS, after);
		InOrder inOrder = inOrder(before, uOptions, after);
		assertThat(((WrapsTimeouts) wOptions.timeouts()).getWrappedTimeouts(), is(equalTo(timeouts)));
		inOrder.verify(before).onTimeouts(uDriver, uOptions);
		inOrder.verify(uOptions).timeouts();
		inOrder.verify(after).onTimeouts(uDriver, uOptions, timeouts);
		inOrder.verifyNoMoreInteractions();
	}
}
