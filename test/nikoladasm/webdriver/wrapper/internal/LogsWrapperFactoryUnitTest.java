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
import static nikoladasm.webdriver.wrapper.internal.LogsWrapperFactory.wrapLogs;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.mockito.InOrder;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.Logs;

import nikoladasm.webdriver.wrapper.UnderlyingWebDriver;
import nikoladasm.webdriver.wrapper.listeners.OnExceptionEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetAvailableLogTypesAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetAvailableLogTypesBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnLogsGetAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnLogsGetBeforeEventListener;

public class LogsWrapperFactoryUnitTest extends ListenableBaseTest {

	private final Logs uLogs = mock(Logs.class);
	private final UnderlyingWebDriver uDriver = mock(UnderlyingWebDriver.class);
	
	private Logs wLogs = wrapLogs(uLogs, uDriver, listeners);
	
	@Test
	public void shouldBeReturnWrappedLogs() {
		assertThat(((WrapsLogs) wLogs).getWrappedLogs(), is(equalTo(uLogs)));
	}
	
	@Test
	public void shouldBeFireOnGetListener() {
		LogEntries logEntries = mock(LogEntries.class);
		when(uLogs.get("testLogType")).thenReturn(logEntries);
		OnLogsGetBeforeEventListener before = mock(OnLogsGetBeforeEventListener.class, CALLS_REAL_METHODS);
		OnLogsGetAfterEventListener after = mock(OnLogsGetAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_LOGS_GET, before);
		setListener(AFTER_LOGS_GET, after);
		InOrder inOrder = inOrder(before, uLogs, after);
		assertThat(wLogs.get("testLogType"), is(equalTo(logEntries)));
		inOrder.verify(before).onGet(uDriver, uLogs, "testLogType");
		inOrder.verify(uLogs).get("testLogType");
		inOrder.verify(after).onGet(uDriver, uLogs, "testLogType", logEntries);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnExceptionListener() {
		RuntimeException exeption = new RuntimeException("testException");
		doThrow(exeption).when(uLogs).get("testLogType");
		OnExceptionEventListener listener = mock(OnExceptionEventListener.class, CALLS_REAL_METHODS);
		setListener(ON_EXCEPTION, listener);
		try {
			wLogs.get("testLogType");
		} catch (Exception e) {
			assertThat(e, is(equalTo(exeption)));
		}
		InOrder inOrder = inOrder(uLogs, listener);
		inOrder.verify(uLogs).get("testLogType");
		inOrder.verify(listener).onException(uDriver, uLogs, exeption, "get");
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnGetAvailableLogTypesListener() {
		Set<String> fAvailableLogTypes = new HashSet<>();
		fAvailableLogTypes.add("testLogType");
		when(uLogs.getAvailableLogTypes()).thenReturn(fAvailableLogTypes);
		OnGetAvailableLogTypesBeforeEventListener before = mock(OnGetAvailableLogTypesBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetAvailableLogTypesAfterEventListener after = mock(OnGetAvailableLogTypesAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_AVAILABLE_LOG_TYPES, before);
		setListener(AFTER_GET_AVAILABLE_LOG_TYPES, after);
		InOrder inOrder = inOrder(before, uLogs, after);
		Set<String> availableLogTypes = wLogs.getAvailableLogTypes();
		assertThat(availableLogTypes.size(), is(equalTo(fAvailableLogTypes.size())));
		assertThat(availableLogTypes.contains("testLogType"), is(equalTo(true)));
		inOrder.verify(before).onGetAvailableLogTypes(uDriver, uLogs);
		inOrder.verify(uLogs).getAvailableLogTypes();
		inOrder.verify(after).onGetAvailableLogTypes(uDriver, uLogs, fAvailableLogTypes);
		inOrder.verifyNoMoreInteractions();
	}
}
