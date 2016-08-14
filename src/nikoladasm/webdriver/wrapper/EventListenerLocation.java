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

package nikoladasm.webdriver.wrapper;

import nikoladasm.webdriver.wrapper.internal.TimeClause;
import nikoladasm.webdriver.wrapper.listeners.*;

import static nikoladasm.webdriver.wrapper.internal.TimeClause.*;

public final class EventListenerLocation<T> {

	public static final EventListenerLocation<OnExceptionEventListener> ON_EXCEPTION =
		valueOf(null, "");
	public static final EventListenerLocation<OnCloseEventListener> BEFORE_CLOSE =
		valueOf(BEFORE, "close");
	public static final EventListenerLocation<OnCloseEventListener> AFTER_CLOSE =
		valueOf(AFTER, "close");
	public static final EventListenerLocation<OnQuitEventListener> BEFORE_QUIT =
		valueOf(BEFORE, "quit");
	public static final EventListenerLocation<OnQuitEventListener> AFTER_QUIT =
		valueOf(AFTER, "quit");
	public static final EventListenerLocation<OnGetEventListener> BEFORE_GET =
		valueOf(BEFORE, "get");
	public static final EventListenerLocation<OnGetEventListener> AFTER_GET =
		valueOf(AFTER, "get");
	public static final EventListenerLocation<OnGetCurrentUrlBeforeEventListener> BEFORE_GET_CURRENT_URL =
		valueOf(BEFORE, "getCurrentUrl");
	public static final EventListenerLocation<OnGetCurrentUrlAfterEventListener> AFTER_GET_CURRENT_URL =
		valueOf(AFTER, "getCurrentUrl");
	public static final EventListenerLocation<OnGetPageSourceBeforeEventListener> BEFORE_GET_PAGE_SOURCE =
		valueOf(BEFORE, "getPageSource");
	public static final EventListenerLocation<OnGetPageSourceAfterEventListener> AFTER_GET_PAGE_SOURCE =
		valueOf(AFTER, "getPageSource");
	public static final EventListenerLocation<OnGetTitleBeforeEventListener> BEFORE_GET_TITLE =
		valueOf(BEFORE, "getTitle");
	public static final EventListenerLocation<OnGetTitleAfterEventListener> AFTER_GET_TITLE =
		valueOf(AFTER, "getTitle");
	public static final EventListenerLocation<OnGetWindowHandleBeforeEventListener> BEFORE_GET_WINDOW_HANDLE =
		valueOf(BEFORE, "getWindowHandle");
	public static final EventListenerLocation<OnGetWindowHandleAfterEventListener> AFTER_GET_WINDOW_HANDLE =
		valueOf(AFTER, "getWindowHandle");
	public static final EventListenerLocation<OnGetWindowHandlesBeforeEventListener> BEFORE_GET_WINDOW_HANDLES =
		valueOf(BEFORE, "getWindowHandles");
	public static final EventListenerLocation<OnGetWindowHandlesAfterEventListener> AFTER_GET_WINDOW_HANDLES =
		valueOf(AFTER, "getWindowHandles");
	public static final EventListenerLocation<OnFindElementBeforeEventListener> BEFORE_FIND_ELEMENT =
		valueOf(BEFORE, "findElement");
	public static final EventListenerLocation<OnFindElementAfterEventListener> AFTER_FIND_ELEMENT =
		valueOf(AFTER, "findElement");
	public static final EventListenerLocation<OnFindElementsBeforeEventListener> BEFORE_FIND_ELEMENTS =
		valueOf(BEFORE, "findElements");
	public static final EventListenerLocation<OnFindElementsAfterEventListener> AFTER_FIND_ELEMENTS =
		valueOf(AFTER, "findElements");
	public static final EventListenerLocation<OnClickEventListener> BEFORE_CLICK =
		valueOf(BEFORE, "click");
	public static final EventListenerLocation<OnClickEventListener> AFTER_CLICK =
		valueOf(AFTER, "click");
	public static final EventListenerLocation<OnClearEventListener> BEFORE_CLEAR =
		valueOf(BEFORE, "clear");
	public static final EventListenerLocation<OnClearEventListener> AFTER_CLEAR =
		valueOf(AFTER, "clear");
	public static final EventListenerLocation<OnSubmitEventListener> BEFORE_SUBMIT =
		valueOf(BEFORE, "submit");
	public static final EventListenerLocation<OnSubmitEventListener> AFTER_SUBMIT =
		valueOf(AFTER, "submit");
	public static final EventListenerLocation<OnGetTagNameBeforeEventListener> BEFORE_GET_TAG_NAME =
		valueOf(BEFORE, "getTagName");
	public static final EventListenerLocation<OnGetTagNameAfterEventListener> AFTER_GET_TAG_NAME =
		valueOf(AFTER, "getTagName");
	public static final EventListenerLocation<OnGetTextBeforeEventListener> BEFORE_GET_TEXT =
		valueOf(BEFORE, "getText");
	public static final EventListenerLocation<OnGetTextAfterEventListener> AFTER_GET_TEXT =
		valueOf(AFTER, "getText");
	public static final EventListenerLocation<OnIsDisplayedBeforeEventListener> BEFORE_IS_DISPLAYED =
		valueOf(BEFORE, "isDisplayed");
	public static final EventListenerLocation<OnIsDisplayedAfterEventListener> AFTER_IS_DISPLAYED =
		valueOf(AFTER, "isDisplayed");
	public static final EventListenerLocation<OnIsEnabledBeforeEventListener> BEFORE_IS_ENABLED =
		valueOf(BEFORE, "isEnabled");
	public static final EventListenerLocation<OnIsEnabledAfterEventListener> AFTER_IS_ENABLED =
		valueOf(AFTER, "isEnabled");
	public static final EventListenerLocation<OnIsSelectedBeforeEventListener> BEFORE_IS_SELECTED =
		valueOf(BEFORE, "isSelected");
	public static final EventListenerLocation<OnIsSelectedAfterEventListener> AFTER_IS_SELECTED =
		valueOf(AFTER, "isSelected");
	public static final EventListenerLocation<OnSendKeysEventListener> BEFORE_SEND_KEYS =
		valueOf(BEFORE, "sendKeys");
	public static final EventListenerLocation<OnSendKeysEventListener> AFTER_SEND_KEYS =
		valueOf(AFTER, "sendKeys");
	public static final EventListenerLocation<OnGetAttributeBeforeEventListener> BEFORE_GET_ATTRIBUTE =
		valueOf(BEFORE, "getAttribute");
	public static final EventListenerLocation<OnGetAttributeAfterEventListener> AFTER_GET_ATTRIBUTE =
		valueOf(AFTER, "getAttribute");
	public static final EventListenerLocation<OnGetCssValueBeforeEventListener> BEFORE_GET_CSS_VALUE =
		valueOf(BEFORE, "getCssValue");
	public static final EventListenerLocation<OnGetCssValueAfterEventListener> AFTER_GET_CSS_VALUE =
		valueOf(AFTER, "getCssValue");
	public static final EventListenerLocation<OnGetLocationBeforeEventListener> BEFORE_GET_LOCATION =
		valueOf(BEFORE, "getLocation");
	public static final EventListenerLocation<OnGetLocationAfterEventListener> AFTER_GET_LOCATION =
		valueOf(AFTER, "getLocation");
	public static final EventListenerLocation<OnGetRectBeforeEventListener> BEFORE_GET_RECT =
		valueOf(BEFORE, "getRect");
	public static final EventListenerLocation<OnGetRectAfterEventListener> AFTER_GET_RECT =
		valueOf(AFTER, "getRect");
	public static final EventListenerLocation<OnGetSizeBeforeEventListener> BEFORE_GET_SIZE =
		valueOf(BEFORE, "getSize");
	public static final EventListenerLocation<OnGetSizeAfterEventListener> AFTER_GET_SIZE =
		valueOf(AFTER, "getSize");
	public static final EventListenerLocation<OnGetScreenshotAsBeforeEventListener> BEFORE_GET_SCREENSHOT_AS =
		valueOf(BEFORE, "getScreenshotAs");
	public static final EventListenerLocation<OnGetScreenshotAsAfterEventListener> AFTER_GET_SCREENSHOT_AS =
		valueOf(AFTER, "getScreenshotAs");
	public static final EventListenerLocation<OnGetCoordinatesBeforeEventListener> BEFORE_GET_COORDINATES =
		valueOf(BEFORE, "getCoordinates");
	public static final EventListenerLocation<OnGetCoordinatesAfterEventListener> AFTER_GET_COORDINATES =
		valueOf(AFTER, "getCoordinates");
	public static final EventListenerLocation<OnNavigateBeforeEventListener> BEFORE_NAVIGATE =
		valueOf(BEFORE, "navigate");
	public static final EventListenerLocation<OnNavigateAfterEventListener> AFTER_NAVIGATE =
		valueOf(AFTER, "navigate");
	public static final EventListenerLocation<OnNavigateBackEventListener> BEFORE_NAVIGATE_BACK =
		valueOf(BEFORE, "back");
	public static final EventListenerLocation<OnNavigateBackEventListener> AFTER_NAVIGATE_BACK =
		valueOf(AFTER, "back");
	public static final EventListenerLocation<OnNavigateForwardEventListener> BEFORE_NAVIGATE_FORWARD =
		valueOf(BEFORE, "forward");
	public static final EventListenerLocation<OnNavigateForwardEventListener> AFTER_NAVIGATE_FORWARD =
		valueOf(AFTER, "forward");
	public static final EventListenerLocation<OnRefreshEventListener> BEFORE_REFRESH =
		valueOf(BEFORE, "refresh");
	public static final EventListenerLocation<OnRefreshEventListener> AFTER_REFRESH =
		valueOf(AFTER, "refresh");
	public static final EventListenerLocation<OnNavigateToEventListener> BEFORE_NAVIGATE_TO =
		valueOf(BEFORE, "to");
	public static final EventListenerLocation<OnNavigateToEventListener> AFTER_NAVIGATE_TO =
		valueOf(AFTER, "to");
	public static final EventListenerLocation<OnSwitchToBeforeEventListener> BEFORE_SWITCH_TO =
		valueOf(BEFORE, "switchTo");
	public static final EventListenerLocation<OnSwitchToAfterEventListener> AFTER_SWITCH_TO =
		valueOf(AFTER, "switchTo");
	public static final EventListenerLocation<OnSwitchToFrameEventListener> BEFORE_SWITCH_TO_FRAME =
		valueOf(BEFORE, "frame");
	public static final EventListenerLocation<OnSwitchToFrameEventListener> AFTER_SWITCH_TO_FRAME =
		valueOf(AFTER, "frame");
	public static final EventListenerLocation<OnSwitchToWindowEventListener> BEFORE_SWITCH_TO_WINDOW =
		valueOf(BEFORE, "window");
	public static final EventListenerLocation<OnSwitchToWindowEventListener> AFTER_SWITCH_TO_WINDOW =
		valueOf(AFTER, "window");
	public static final EventListenerLocation<OnSwitchToDefaultContentEventListener> BEFORE_SWITCH_TO_DEFAULT_CONTENT =
		valueOf(BEFORE, "defaultContent");
	public static final EventListenerLocation<OnSwitchToDefaultContentEventListener> AFTER_SWITCH_TO_DEFAULT_CONTENT =
		valueOf(AFTER, "defaultContent");
	public static final EventListenerLocation<OnSwitchToParentFrameEventListener> BEFORE_SWITCH_TO_PARENT_FRAME =
		valueOf(BEFORE, "parentFrame");
	public static final EventListenerLocation<OnSwitchToParentFrameEventListener> AFTER_SWITCH_TO_PARENT_FRAME =
		valueOf(AFTER, "parentFrame");
	public static final EventListenerLocation<OnSwitchToActiveElementBeforeEventListener> BEFORE_SWITCH_TO_ACTIVE_ELEMENT =
		valueOf(BEFORE, "activeElement");
	public static final EventListenerLocation<OnSwitchToActiveElementAfterEventListener> AFTER_SWITCH_TO_ACTIVE_ELEMENT =
		valueOf(AFTER, "activeElement");
	public static final EventListenerLocation<OnSwitchToAlertBeforeEventListener> BEFORE_SWITCH_TO_ALERT =
		valueOf(BEFORE, "alert");
	public static final EventListenerLocation<OnSwitchToAlertAfterEventListener> AFTER_SWITCH_TO_ALERT =
		valueOf(AFTER, "alert");
	public static final EventListenerLocation<OnAcceptEventListener> BEFORE_ACCEPT =
		valueOf(BEFORE, "accept");
	public static final EventListenerLocation<OnAcceptEventListener> AFTER_ACCEPT =
		valueOf(AFTER, "accept");
	public static final EventListenerLocation<OnDismissEventListener> BEFORE_DISMISS =
		valueOf(BEFORE, "dismiss");
	public static final EventListenerLocation<OnDismissEventListener> AFTER_DISMISS =
		valueOf(AFTER, "dismiss");
	public static final EventListenerLocation<OnGetTextOfAlertBeforeEventListener> BEFORE_GET_TEXT_OF_ALERT =
		valueOf(BEFORE, "getText");
	public static final EventListenerLocation<OnGetTextOfAlertAfterEventListener> AFTER_GET_TEXT_OF_ALERT =
		valueOf(AFTER, "getText");
	public static final EventListenerLocation<OnSendKeysToAlertEventListener> BEFORE_SEND_KEYS_TO_ALERT =
		valueOf(BEFORE, "sendKeys");
	public static final EventListenerLocation<OnSendKeysToAlertEventListener> AFTER_SEND_KEYS_TO_ALERT =
		valueOf(AFTER, "sendKeys");
	public static final EventListenerLocation<OnAuthenticateUsingEventListener> BEFORE_AUTHENTICATE_USING =
		valueOf(BEFORE, "authenticateUsing");
	public static final EventListenerLocation<OnAuthenticateUsingEventListener> AFTER_AUTHENTICATE_USING =
		valueOf(AFTER, "authenticateUsing");
	public static final EventListenerLocation<OnSetCredentialsEventListener> BEFORE_SET_CREDENTIALS =
		valueOf(BEFORE, "setCredentials");
	public static final EventListenerLocation<OnSetCredentialsEventListener> AFTER_SET_CREDENTIALS =
		valueOf(AFTER, "setCredentials");
	public static final EventListenerLocation<OnManageBeforeEventListener> BEFORE_MANAGE =
		valueOf(BEFORE, "manage");
	public static final EventListenerLocation<OnManageAfterEventListener> AFTER_MANAGE =
		valueOf(AFTER, "manage");
	public static final EventListenerLocation<OnAddCookieEventListener> BEFORE_ADD_COOKIE =
		valueOf(BEFORE, "addCookie");
	public static final EventListenerLocation<OnAddCookieEventListener> AFTER_ADD_COOKIE =
		valueOf(AFTER, "addCookie");
	public static final EventListenerLocation<OnDeleteCookieEventListener> BEFORE_DELETE_COOKIE =
		valueOf(BEFORE, "deleteCookie");
	public static final EventListenerLocation<OnDeleteCookieEventListener> AFTER_DELETE_COOKIE =
		valueOf(AFTER, "deleteCookie");
	public static final EventListenerLocation<OnDeleteAllCookiesEventListener> BEFORE_DELETE_ALL_COOKIES =
		valueOf(BEFORE, "deleteAllCookies");
	public static final EventListenerLocation<OnDeleteAllCookiesEventListener> AFTER_DELETE_ALL_COOKIES =
		valueOf(AFTER, "deleteAllCookies");
	public static final EventListenerLocation<OnGetCookieNamedBeforeEventListener> BEFORE_GET_COOKIE_NAMED =
		valueOf(BEFORE, "getCookieNamed");
	public static final EventListenerLocation<OnGetCookieNamedAfterEventListener> AFTER_GET_COOKIE_NAMED =
		valueOf(AFTER, "getCookieNamed");
	public static final EventListenerLocation<OnDeleteCookieNamedEventListener> BEFORE_DELETE_COOKIE_NAMED =
		valueOf(BEFORE, "deleteCookieNamed");
	public static final EventListenerLocation<OnDeleteCookieNamedEventListener> AFTER_DELETE_COOKIE_NAMED =
		valueOf(AFTER, "deleteCookieNamed");
	public static final EventListenerLocation<OnGetCookiesBeforeEventListener> BEFORE_GET_COOKIES =
		valueOf(BEFORE, "getCookies");
	public static final EventListenerLocation<OnGetCookiesAfterEventListener> AFTER_GET_COOKIES =
		valueOf(AFTER, "getCookies");
	public static final EventListenerLocation<OnLogsBeforeEventListener> BEFORE_LOGS =
		valueOf(BEFORE, "logs");
	public static final EventListenerLocation<OnLogsAfterEventListener> AFTER_LOGS =
		valueOf(AFTER, "logs");
	public static final EventListenerLocation<OnLogsGetBeforeEventListener> BEFORE_LOGS_GET =
		valueOf(BEFORE, "get");
	public static final EventListenerLocation<OnLogsGetAfterEventListener> AFTER_LOGS_GET =
		valueOf(AFTER, "get");
	public static final EventListenerLocation<OnGetAvailableLogTypesBeforeEventListener> BEFORE_GET_AVAILABLE_LOG_TYPES =
		valueOf(BEFORE, "getAvailableLogTypes");
	public static final EventListenerLocation<OnGetAvailableLogTypesAfterEventListener> AFTER_GET_AVAILABLE_LOG_TYPES =
		valueOf(AFTER, "getAvailableLogTypes");
	public static final EventListenerLocation<OnImeBeforeEventListener> BEFORE_IME =
		valueOf(BEFORE, "ime");
	public static final EventListenerLocation<OnImeAfterEventListener> AFTER_IME =
		valueOf(AFTER, "ime");
	public static final EventListenerLocation<OnGetAvailableEnginesBeforeEventListener> BEFORE_GET_AVAILABLE_ENGINES =
		valueOf(BEFORE, "getAvailableEngines");
	public static final EventListenerLocation<OnGetAvailableEnginesAfterEventListener> AFTER_GET_AVAILABLE_ENGINES =
		valueOf(AFTER, "getAvailableEngines");
	public static final EventListenerLocation<OnActivateEngineEventListener> BEFORE_ACTIVATE_ENGINE =
		valueOf(BEFORE, "activateEngine");
	public static final EventListenerLocation<OnActivateEngineEventListener> AFTER_ACTIVATE_ENGINE =
		valueOf(AFTER, "activateEngine");
	public static final EventListenerLocation<OnDeactivateEventListener> BEFORE_DEACTIVATE =
		valueOf(BEFORE, "deactivate");
	public static final EventListenerLocation<OnDeactivateEventListener> AFTER_DEACTIVATE =
		valueOf(AFTER, "deactivate");
	public static final EventListenerLocation<OnGetActiveEngineBeforeEventListener> BEFORE_GET_ACTIVE_ENGINE =
		valueOf(BEFORE, "getActiveEngine");
	public static final EventListenerLocation<OnGetActiveEngineAfterEventListener> AFTER_GET_ACTIVE_ENGINE =
		valueOf(AFTER, "getActiveEngine");
	public static final EventListenerLocation<OnIsActivatedBeforeEventListener> BEFORE_IS_ACTIVATED =
		valueOf(BEFORE, "isActivated");
	public static final EventListenerLocation<OnIsActivatedAfterEventListener> AFTER_IS_ACTIVATED =
		valueOf(AFTER, "isActivated");
	public static final EventListenerLocation<OnWindowBeforeEventListener> BEFORE_WINDOW =
		valueOf(BEFORE, "window");
	public static final EventListenerLocation<OnWindowAfterEventListener> AFTER_WINDOW =
		valueOf(AFTER, "window");
	public static final EventListenerLocation<OnFullscreenEventListener> BEFORE_FULLSCREEN =
		valueOf(BEFORE, "fullscreen");
	public static final EventListenerLocation<OnFullscreenEventListener> AFTER_FULLSCREEN =
		valueOf(AFTER, "fullscreen");
	public static final EventListenerLocation<OnMaximizeEventListener> BEFORE_MAXIMIZE =
		valueOf(BEFORE, "maximize");
	public static final EventListenerLocation<OnMaximizeEventListener> AFTER_MAXIMIZE =
		valueOf(AFTER, "maximize");
	public static final EventListenerLocation<OnGetPositionBeforeEventListener> BEFORE_GET_POSITION =
		valueOf(BEFORE, "getPosition");
	public static final EventListenerLocation<OnGetPositionAfterEventListener> AFTER_GET_POSITION =
		valueOf(AFTER, "getPosition");
	public static final EventListenerLocation<OnGetWindowSizeBeforeEventListener> BEFORE_GET_WINDOW_SIZE =
		valueOf(BEFORE, "getSize");
	public static final EventListenerLocation<OnGetWindowSizeAfterEventListener> AFTER_GET_WINDOW_SIZE =
		valueOf(AFTER, "getSize");
	public static final EventListenerLocation<OnSetPositionEventListener> BEFORE_SET_POSITION =
		valueOf(BEFORE, "setPosition");
	public static final EventListenerLocation<OnSetPositionEventListener> AFTER_SET_POSITION =
		valueOf(AFTER, "setPosition");
	public static final EventListenerLocation<OnSetWindowSizeEventListener> BEFORE_SET_WINDOW_SIZE =
		valueOf(BEFORE, "setSize");
	public static final EventListenerLocation<OnSetWindowSizeEventListener> AFTER_SET_WINDOW_SIZE =
		valueOf(AFTER, "setSize");
	public static final EventListenerLocation<OnTimeoutsBeforeEventListener> BEFORE_TIMEOUTS =
		valueOf(BEFORE, "timeouts");
	public static final EventListenerLocation<OnTimeoutsAfterEventListener> AFTER_TIMEOUTS =
		valueOf(AFTER, "timeouts");
	public static final EventListenerLocation<OnImplicitlyWaitEventListener> BEFORE_IMPLICITLY_WAIT =
		valueOf(BEFORE, "implicitlyWait");
	public static final EventListenerLocation<OnImplicitlyWaitEventListener> AFTER_IMPLICITLY_WAIT =
		valueOf(AFTER, "implicitlyWait");
	public static final EventListenerLocation<OnSetScriptTimeoutEventListener> BEFORE_SET_SCRIPT_TIMEOUT =
		valueOf(BEFORE, "setScriptTimeout");
	public static final EventListenerLocation<OnSetScriptTimeoutEventListener> AFTER_SET_SCRIPT_TIMEOUT =
		valueOf(AFTER, "setScriptTimeout");
	public static final EventListenerLocation<OnPageLoadTimeoutEventListener> BEFORE_PAGE_LOAD_TIMEOUT =
		valueOf(BEFORE, "pageLoadTimeout");
	public static final EventListenerLocation<OnPageLoadTimeoutEventListener> AFTER_PAGE_LOAD_TIMEOUT =
		valueOf(AFTER, "pageLoadTimeout");
	public static final EventListenerLocation<OnGetKeyboardBeforeEventListener> BEFORE_GET_KEYBOARD =
		valueOf(BEFORE, "getKeyboard");
	public static final EventListenerLocation<OnGetKeyboardAfterEventListener> AFTER_GET_KEYBOARD =
		valueOf(AFTER, "getKeyboard");
	public static final EventListenerLocation<OnPressKeyEventListener> BEFORE_PRESS_KEY =
		valueOf(BEFORE, "pressKey");
	public static final EventListenerLocation<OnPressKeyEventListener> AFTER_PRESS_KEY =
		valueOf(AFTER, "pressKey");
	public static final EventListenerLocation<OnReleaseKeyEventListener> BEFORE_RELEASE_KEY =
		valueOf(BEFORE, "releaseKey");
	public static final EventListenerLocation<OnReleaseKeyEventListener> AFTER_RELEASE_KEY =
		valueOf(AFTER, "releaseKey");
	public static final EventListenerLocation<OnSendKeysFromKeyboardEventListener> BEFORE_SEND_KEYS_FROM_KEYBOARD =
		valueOf(BEFORE, "sendKeys");
	public static final EventListenerLocation<OnSendKeysFromKeyboardEventListener> AFTER_SEND_KEYS_FROM_KEYBOARD =
		valueOf(AFTER, "sendKeys");
	public static final EventListenerLocation<OnGetMouseBeforeEventListener> BEFORE_GET_MOUSE =
		valueOf(BEFORE, "getMouse");
	public static final EventListenerLocation<OnGetMouseAfterEventListener> AFTER_GET_MOUSE =
		valueOf(AFTER, "getMouse");
	public static final EventListenerLocation<OnMouseClickEventListener> BEFORE_MOUSE_CLICK =
		valueOf(BEFORE, "click");
	public static final EventListenerLocation<OnMouseClickEventListener> AFTER_MOUSE_CLICK =
		valueOf(AFTER, "click");
	public static final EventListenerLocation<OnContextClickEventListener> BEFORE_CONTEXT_CLICK =
		valueOf(BEFORE, "contextClick");
	public static final EventListenerLocation<OnContextClickEventListener> AFTER_CONTEXT_CLICK =
		valueOf(AFTER, "contextClick");
	public static final EventListenerLocation<OnDoubleClickEventListener> BEFORE_DOUBLE_CLICK =
		valueOf(BEFORE, "doubleClick");
	public static final EventListenerLocation<OnDoubleClickEventListener> AFTER_DOUBLE_CLICK =
		valueOf(AFTER, "doubleClick");
	public static final EventListenerLocation<OnMouseDownEventListener> BEFORE_MOUSE_DOWN =
		valueOf(BEFORE, "mouseDown");
	public static final EventListenerLocation<OnMouseDownEventListener> AFTER_MOUSE_DOWN =
		valueOf(AFTER, "mouseDown");
	public static final EventListenerLocation<OnMouseUpEventListener> BEFORE_MOUSE_UP =
		valueOf(BEFORE, "mouseUp");
	public static final EventListenerLocation<OnMouseUpEventListener> AFTER_MOUSE_UP =
		valueOf(AFTER, "mouseUp");
	public static final EventListenerLocation<OnMouseMoveEventListener> BEFORE_MOUSE_MOVE =
		valueOf(BEFORE, "mouseMove");
	public static final EventListenerLocation<OnMouseMoveEventListener> AFTER_MOUSE_MOVE =
		valueOf(AFTER, "mouseMove");
	public static final EventListenerLocation<OnGetTouchBeforeEventListener> BEFORE_GET_TOUCH =
		valueOf(BEFORE, "getTouch");
	public static final EventListenerLocation<OnGetTouchAfterEventListener> AFTER_GET_TOUCH =
		valueOf(AFTER, "getTouch");
	public static final EventListenerLocation<OnSingleTapEventListener> BEFORE_SINGLE_TAP =
		valueOf(BEFORE, "singleTap");
	public static final EventListenerLocation<OnSingleTapEventListener> AFTER_SINGLE_TAP =
		valueOf(AFTER, "singleTap");
	public static final EventListenerLocation<OnDoubleTapEventListener> BEFORE_DOUBLE_TAP =
		valueOf(BEFORE, "doubleTap");
	public static final EventListenerLocation<OnDoubleTapEventListener> AFTER_DOUBLE_TAP =
		valueOf(AFTER, "doubleTap");
	public static final EventListenerLocation<OnLongPressEventListener> BEFORE_LONG_PRESS =
		valueOf(BEFORE, "longPress");
	public static final EventListenerLocation<OnLongPressEventListener> AFTER_LONG_PRESS =
		valueOf(AFTER, "longPress");
	public static final EventListenerLocation<OnTouchUpEventListener> BEFORE_TOUCH_UP =
		valueOf(BEFORE, "up");
	public static final EventListenerLocation<OnTouchUpEventListener> AFTER_TOUCH_UP =
		valueOf(AFTER, "up");
	public static final EventListenerLocation<OnTouchDownEventListener> BEFORE_TOUCH_DOWN =
		valueOf(BEFORE, "down");
	public static final EventListenerLocation<OnTouchDownEventListener> AFTER_TOUCH_DOWN =
		valueOf(AFTER, "down");
	public static final EventListenerLocation<OnTouchMoveEventListener> BEFORE_TOUCH_MOVE =
		valueOf(BEFORE, "move");
	public static final EventListenerLocation<OnTouchMoveEventListener> AFTER_TOUCH_MOVE =
		valueOf(AFTER, "move");
	public static final EventListenerLocation<OnTouchScrollEventListener> BEFORE_TOUCH_SCROLL =
		valueOf(BEFORE, "scroll");
	public static final EventListenerLocation<OnTouchScrollEventListener> AFTER_TOUCH_SCROLL =
		valueOf(AFTER, "scroll");
	public static final EventListenerLocation<OnTouchFlickEventListener> BEFORE_TOUCH_FLICK =
		valueOf(BEFORE, "flick");
	public static final EventListenerLocation<OnTouchFlickEventListener> AFTER_TOUCH_FLICK =
		valueOf(AFTER, "flick");
	public static final EventListenerLocation<OnExecuteScriptBeforeEventListener> BEFORE_EXECUTE_SCRIPT =
		valueOf(BEFORE, "executeScript");
	public static final EventListenerLocation<OnExecuteScriptAfterEventListener> AFTER_EXECUTE_SCRIPT =
		valueOf(AFTER, "executeScript");
	public static final EventListenerLocation<OnExecuteAsyncScriptBeforeEventListener> BEFORE_EXECUTE_ASYNC_SCRIPT =
		valueOf(BEFORE, "executeAsyncScript");
	public static final EventListenerLocation<OnExecuteAsyncScriptAfterEventListener> AFTER_EXECUTE_ASYNC_SCRIPT =
		valueOf(AFTER, "executeAsyncScript");
	
	private static <T> EventListenerLocation<T> valueOf(TimeClause timeClause, String methodName) {
		return new EventListenerLocation<T>(methodName, timeClause);
	}
	
	private String methodName;
	private TimeClause timeClause;
	
	private EventListenerLocation(String methodName, TimeClause timeClause) {
		this.methodName = methodName;
		this.timeClause = timeClause;
	}
	
	public TimeClause timeClause() {
		return timeClause;
	}
	
	public String name() {
		return methodName;
	}
	
	@Override
	public String toString() {
		return (timeClause == AFTER  ? "after " : "before ") + methodName;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
