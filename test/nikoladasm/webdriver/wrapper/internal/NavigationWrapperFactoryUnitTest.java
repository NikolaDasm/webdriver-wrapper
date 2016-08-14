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
import static nikoladasm.webdriver.wrapper.internal.NavigationWrapperFactory.wrapNavigation;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.mockito.InOrder;
import org.openqa.selenium.WebDriver.Navigation;

import nikoladasm.webdriver.wrapper.UnderlyingWebDriver;
import nikoladasm.webdriver.wrapper.listeners.OnExceptionEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnNavigateBackEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnNavigateForwardEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnNavigateToEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnRefreshEventListener;

public class NavigationWrapperFactoryUnitTest extends ListenableBaseTest {

	private final Navigation uNavigation = mock(Navigation.class);
	private final UnderlyingWebDriver uDriver = mock(UnderlyingWebDriver.class);
	
	private Navigation wNavigation = wrapNavigation(uNavigation, uDriver, listeners);
	
	@Test
	public void shouldBeReturnWrappedNavigation() {
		assertThat(((WrapsNavigation) wNavigation).getWrappedNavigation(), is(equalTo(uNavigation)));
	}
	
	@Test
	public void shouldBeFireOnBackListener() {
		OnNavigateBackEventListener before = mock(OnNavigateBackEventListener.class, CALLS_REAL_METHODS);
		OnNavigateBackEventListener after = mock(OnNavigateBackEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_NAVIGATE_BACK, before);
		setListener(AFTER_NAVIGATE_BACK, after);
		InOrder inOrder = inOrder(before, uNavigation, after);
		wNavigation.back();
		inOrder.verify(before).onBack(uDriver, uNavigation);
		inOrder.verify(uNavigation).back();
		inOrder.verify(after).onBack(uDriver, uNavigation);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnExceptionListener() {
		OnExceptionEventListener listener = mock(OnExceptionEventListener.class, CALLS_REAL_METHODS);
		setListener(ON_EXCEPTION, listener);
		RuntimeException exeption = new RuntimeException("testException");
		doThrow(exeption).when(uNavigation).back();
		try {
			wNavigation.back();
		} catch (Exception e) {
			assertThat(e, is(equalTo(exeption)));
		}
		InOrder inOrder = inOrder(uNavigation, listener);
		inOrder.verify(uNavigation).back();
		inOrder.verify(listener).onException(uDriver, uNavigation, exeption, "back");
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnForwardListener() {
		OnNavigateForwardEventListener before = mock(OnNavigateForwardEventListener.class, CALLS_REAL_METHODS);
		OnNavigateForwardEventListener after = mock(OnNavigateForwardEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_NAVIGATE_FORWARD, before);
		setListener(AFTER_NAVIGATE_FORWARD, after);
		InOrder inOrder = inOrder(before, uNavigation, after);
		wNavigation.forward();
		inOrder.verify(before).onForward(uDriver, uNavigation);
		inOrder.verify(uNavigation).forward();
		inOrder.verify(after).onForward(uDriver, uNavigation);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnRefreshListener() {
		OnRefreshEventListener before = mock(OnRefreshEventListener.class, CALLS_REAL_METHODS);
		OnRefreshEventListener after = mock(OnRefreshEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_REFRESH, before);
		setListener(AFTER_REFRESH, after);
		InOrder inOrder = inOrder(before, uNavigation, after);
		wNavigation.refresh();
		inOrder.verify(before).onRefresh(uDriver, uNavigation);
		inOrder.verify(uNavigation).refresh();
		inOrder.verify(after).onRefresh(uDriver, uNavigation);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnNavigateToListener() throws MalformedURLException {
		OnNavigateToEventListener before = mock(OnNavigateToEventListener.class, CALLS_REAL_METHODS);
		OnNavigateToEventListener after = mock(OnNavigateToEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_NAVIGATE_TO, before);
		setListener(AFTER_NAVIGATE_TO, after);
		InOrder inOrder = inOrder(before, uNavigation, after);
		wNavigation.to("testUrl");
		wNavigation.to(new URL("file://testUrl"));
		inOrder.verify(before).onTo(uDriver, uNavigation, "testUrl");
		inOrder.verify(uNavigation).to("testUrl");
		inOrder.verify(after).onTo(uDriver, uNavigation, "testUrl");
		inOrder.verify(before).onTo(uDriver, uNavigation, "file://testUrl");
		inOrder.verify(uNavigation).to(new URL("file://testUrl"));
		inOrder.verify(after).onTo(uDriver, uNavigation, "file://testUrl");
		inOrder.verifyNoMoreInteractions();
	}
}
