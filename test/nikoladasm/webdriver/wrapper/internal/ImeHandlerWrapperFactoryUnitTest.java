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
import static nikoladasm.webdriver.wrapper.internal.ImeHandlerWrapperFactory.wrapImeHandler;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;
import org.openqa.selenium.WebDriver.ImeHandler;

import nikoladasm.webdriver.wrapper.UnderlyingWebDriver;
import nikoladasm.webdriver.wrapper.listeners.OnActivateEngineEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnDeactivateEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnExceptionEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetActiveEngineAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetActiveEngineBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetAvailableEnginesAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetAvailableEnginesBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnIsActivatedAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnIsActivatedBeforeEventListener;

public class ImeHandlerWrapperFactoryUnitTest extends ListenableBaseTest {

	private final ImeHandler uImeHandler = mock(ImeHandler.class);
	private final UnderlyingWebDriver uDriver = mock(UnderlyingWebDriver.class);
	
	private ImeHandler wImeHandler = wrapImeHandler(uImeHandler, uDriver, listeners);
	
	@Test
	public void shouldBeReturnWrappedImeHandler() {
		assertThat(((WrapsImeHandler) wImeHandler).getWrappedImeHandler(), is(equalTo(uImeHandler)));
	}
	
	@Test
	public void shouldBeFireOnGetAvailableEnginesListener() {
		List<String> fAvailableEngines = new ArrayList<>();
		fAvailableEngines.add("testEngine");
		when(uImeHandler.getAvailableEngines()).thenReturn(fAvailableEngines);
		OnGetAvailableEnginesBeforeEventListener before = mock(OnGetAvailableEnginesBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetAvailableEnginesAfterEventListener after = mock(OnGetAvailableEnginesAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_AVAILABLE_ENGINES, before);
		setListener(AFTER_GET_AVAILABLE_ENGINES, after);
		InOrder inOrder = inOrder(before, uImeHandler, after);
		List<String> availableLogTypes = wImeHandler.getAvailableEngines();
		assertThat(availableLogTypes.size(), is(equalTo(fAvailableEngines.size())));
		assertThat(availableLogTypes.get(0), is(equalTo("testEngine")));
		inOrder.verify(before).onGetAvailableEngines(uDriver, uImeHandler);
		inOrder.verify(uImeHandler).getAvailableEngines();
		inOrder.verify(after).onGetAvailableEngines(uDriver, uImeHandler, fAvailableEngines);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnActivateEngineListener() {
		OnActivateEngineEventListener before = mock(OnActivateEngineEventListener.class, CALLS_REAL_METHODS);
		OnActivateEngineEventListener after = mock(OnActivateEngineEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_ACTIVATE_ENGINE, before);
		setListener(AFTER_ACTIVATE_ENGINE, after);
		InOrder inOrder = inOrder(before, uImeHandler, after);
		wImeHandler.activateEngine("testEngine");
		inOrder.verify(before).onActivateEngine(uDriver, uImeHandler, "testEngine");
		inOrder.verify(uImeHandler).activateEngine("testEngine");
		inOrder.verify(after).onActivateEngine(uDriver, uImeHandler, "testEngine");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnExceptionListener() {
		RuntimeException exeption = new RuntimeException("testException");
		doThrow(exeption).when(uImeHandler).activateEngine("testEngine");
		OnExceptionEventListener listener = mock(OnExceptionEventListener.class, CALLS_REAL_METHODS);
		setListener(ON_EXCEPTION, listener);
		try {
			wImeHandler.activateEngine("testEngine");
		} catch (Exception e) {
			assertThat(e, is(equalTo(exeption)));
		}
		InOrder inOrder = inOrder(uImeHandler, listener);
		inOrder.verify(uImeHandler).activateEngine("testEngine");
		inOrder.verify(listener).onException(uDriver, uImeHandler, exeption, "activateEngine");
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnDeactivateListener() {
		OnDeactivateEventListener before = mock(OnDeactivateEventListener.class, CALLS_REAL_METHODS);
		OnDeactivateEventListener after = mock(OnDeactivateEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_DEACTIVATE, before);
		setListener(AFTER_DEACTIVATE, after);
		InOrder inOrder = inOrder(before, uImeHandler, after);
		wImeHandler.deactivate();
		inOrder.verify(before).onDeactivate(uDriver, uImeHandler);
		inOrder.verify(uImeHandler).deactivate();
		inOrder.verify(after).onDeactivate(uDriver, uImeHandler);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetActiveEngineListener() {
		when(uImeHandler.getActiveEngine()).thenReturn("testEngine");
		OnGetActiveEngineBeforeEventListener before = mock(OnGetActiveEngineBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetActiveEngineAfterEventListener after = mock(OnGetActiveEngineAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_ACTIVE_ENGINE, before);
		setListener(AFTER_GET_ACTIVE_ENGINE, after);
		InOrder inOrder = inOrder(before, uImeHandler, after);
		assertThat(wImeHandler.getActiveEngine(), is(equalTo("testEngine")));
		inOrder.verify(before).onGetActiveEngine(uDriver, uImeHandler);
		inOrder.verify(uImeHandler).getActiveEngine();
		inOrder.verify(after).onGetActiveEngine(uDriver, uImeHandler, "testEngine");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnIsActivatedListener() {
		when(uImeHandler.isActivated()).thenReturn(true);
		OnIsActivatedBeforeEventListener before = mock(OnIsActivatedBeforeEventListener.class, CALLS_REAL_METHODS);
		OnIsActivatedAfterEventListener after = mock(OnIsActivatedAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_IS_ACTIVATED, before);
		setListener(AFTER_IS_ACTIVATED, after);
		InOrder inOrder = inOrder(before, uImeHandler, after);
		assertThat(wImeHandler.isActivated(), is(equalTo(true)));
		inOrder.verify(before).onIsActivated(uDriver, uImeHandler);
		inOrder.verify(uImeHandler).isActivated();
		inOrder.verify(after).onIsActivated(uDriver, uImeHandler, true);
		inOrder.verifyNoMoreInteractions();
	}
}
