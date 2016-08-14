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
import static nikoladasm.webdriver.wrapper.internal.AlertWrapperFactory.wrapAlert;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.security.Credentials;

import nikoladasm.webdriver.wrapper.UnderlyingWebDriver;
import nikoladasm.webdriver.wrapper.listeners.OnAcceptEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnAuthenticateUsingEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnDismissEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnExceptionEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetTextOfAlertAfterEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnGetTextOfAlertBeforeEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSendKeysToAlertEventListener;
import nikoladasm.webdriver.wrapper.listeners.OnSetCredentialsEventListener;

public class AlertWrapperFactoryUnitTest extends ListenableBaseTest {

	private final Alert uAlert = mock(Alert.class);
	private final UnderlyingWebDriver uDriver = mock(UnderlyingWebDriver.class);
	
	private Alert wAlert = wrapAlert(uAlert, uDriver, listeners);
	
	@Test
	public void shouldBeReturnWrappedAlert() {
		assertThat(((WrapsAlert) wAlert).getWrappedAlert(), is(equalTo(uAlert)));
	}

	@Test
	public void shouldBeFireOnAcceptListener() {
		OnAcceptEventListener before = mock(OnAcceptEventListener.class, CALLS_REAL_METHODS);
		OnAcceptEventListener after = mock(OnAcceptEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_ACCEPT, before);
		setListener(AFTER_ACCEPT, after);
		InOrder inOrder = inOrder(before, uAlert, after);
		wAlert.accept();
		inOrder.verify(before).onAccept(uDriver, uAlert);
		inOrder.verify(uAlert).accept();
		inOrder.verify(after).onAccept(uDriver, uAlert);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnExceptionListener() {
		OnExceptionEventListener listener = mock(OnExceptionEventListener.class, CALLS_REAL_METHODS);
		setListener(ON_EXCEPTION, listener);
		RuntimeException exeption = new RuntimeException("testException");
		doThrow(exeption).when(uAlert).accept();
		try {
			wAlert.accept();
		} catch (Exception e) {
			assertThat(e, is(equalTo(exeption)));
		}
		InOrder inOrder = inOrder(uAlert, listener);
		inOrder.verify(uAlert).accept();
		inOrder.verify(listener).onException(uDriver, uAlert, exeption, "accept");
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void shouldBeFireOnDismissListener() {
		OnDismissEventListener before = mock(OnDismissEventListener.class, CALLS_REAL_METHODS);
		OnDismissEventListener after = mock(OnDismissEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_DISMISS, before);
		setListener(AFTER_DISMISS, after);
		InOrder inOrder = inOrder(before, uAlert, after);
		wAlert.dismiss();
		inOrder.verify(before).onDismiss(uDriver, uAlert);
		inOrder.verify(uAlert).dismiss();
		inOrder.verify(after).onDismiss(uDriver, uAlert);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnGetTextListener() {
		when(uAlert.getText()).thenReturn("testText");
		OnGetTextOfAlertBeforeEventListener before = mock(OnGetTextOfAlertBeforeEventListener.class, CALLS_REAL_METHODS);
		OnGetTextOfAlertAfterEventListener after = mock(OnGetTextOfAlertAfterEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_GET_TEXT_OF_ALERT, before);
		setListener(AFTER_GET_TEXT_OF_ALERT, after);
		InOrder inOrder = inOrder(before, uAlert, after);
		assertThat(wAlert.getText(), is(equalTo("testText")));
		inOrder.verify(before).onGetText(uDriver, uAlert);
		inOrder.verify(uAlert).getText();
		inOrder.verify(after).onGetText(uDriver, uAlert, "testText");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnSendKeysListener() {
		OnSendKeysToAlertEventListener before = mock(OnSendKeysToAlertEventListener.class, CALLS_REAL_METHODS);
		OnSendKeysToAlertEventListener after = mock(OnSendKeysToAlertEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_SEND_KEYS_TO_ALERT, before);
		setListener(AFTER_SEND_KEYS_TO_ALERT, after);
		InOrder inOrder = inOrder(before, uAlert, after);
		wAlert.sendKeys("testKeys");
		inOrder.verify(before).onSendKeys(uDriver, uAlert, "testKeys");
		inOrder.verify(uAlert).sendKeys("testKeys");
		inOrder.verify(after).onSendKeys(uDriver, uAlert, "testKeys");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnAuthenticateUsingListener() {
		OnAuthenticateUsingEventListener before = mock(OnAuthenticateUsingEventListener.class, CALLS_REAL_METHODS);
		OnAuthenticateUsingEventListener after = mock(OnAuthenticateUsingEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_AUTHENTICATE_USING, before);
		setListener(AFTER_AUTHENTICATE_USING, after);
		InOrder inOrder = inOrder(before, uAlert, after);
		Credentials credentials = mock(Credentials.class);
		wAlert.authenticateUsing(credentials);
		inOrder.verify(before).onAuthenticateUsing(uDriver, uAlert, credentials);
		inOrder.verify(uAlert).authenticateUsing(credentials);
		inOrder.verify(after).onAuthenticateUsing(uDriver, uAlert, credentials);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void shouldBeFireOnSetCredentialsListener() {
		OnSetCredentialsEventListener before = mock(OnSetCredentialsEventListener.class, CALLS_REAL_METHODS);
		OnSetCredentialsEventListener after = mock(OnSetCredentialsEventListener.class, CALLS_REAL_METHODS);
		setListener(BEFORE_SET_CREDENTIALS, before);
		setListener(AFTER_SET_CREDENTIALS, after);
		InOrder inOrder = inOrder(before, uAlert, after);
		Credentials credentials = mock(Credentials.class);
		wAlert.setCredentials(credentials);
		inOrder.verify(before).onSetCredentials(uDriver, uAlert, credentials);
		inOrder.verify(uAlert).setCredentials(credentials);
		inOrder.verify(after).onSetCredentials(uDriver, uAlert, credentials);
		inOrder.verifyNoMoreInteractions();
	}
}
