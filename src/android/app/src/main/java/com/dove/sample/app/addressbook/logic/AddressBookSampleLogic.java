/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.app.addressbook.logic;

import java.io.IOException;
import java.util.Iterator;

import com.dove.sample.app.addressbook.Const;
import com.dove.sample.wrapper.client.BasicRestContext;
import com.dove.sample.wrapper.common.EmptyResponseBody;
import com.dove.sample.wrapper.common.ErrorResponseBody;
import com.dove.sample.wrapper.common.ErrorResponseBody.Errors;
import com.dove.sample.wrapper.common.ErrorResponseBody.ErrorsArray;
import com.dove.sample.wrapper.common.InvalidResponseException;
import com.dove.sample.wrapper.common.Request;
import com.dove.sample.wrapper.common.RequestHeader;
import com.dove.sample.wrapper.common.RequestQuery;
import com.dove.sample.wrapper.common.Response;
import com.dove.sample.wrapper.rws.addressbook.Addressbook;
import com.dove.sample.wrapper.rws.addressbook.CreateEntryRequestBody;
import com.dove.sample.wrapper.rws.addressbook.CreateEntryResponseBody;
import com.dove.sample.wrapper.rws.addressbook.GetEntryListResponseBody;
import com.dove.sample.wrapper.rws.addressbook.GetEntryResponseBody;
import com.dove.sample.wrapper.rws.addressbook.UpdateEntryRequestBody;
import com.dove.sample.wrapper.rws.addressbook.UpdateEntryResponseBody;
import com.dove.sample.wrapper.rws.counter.Counter;
import com.dove.sample.wrapper.rws.counter.GetEcoCounterResponseBody;
import com.dove.sample.wrapper.rws.counter.GetUserCounterResponseBody;
import android.util.Log;

/**
 * アドレス帳サンプルアプリの WebAPI による情報取得、追加、更新ロジックをまとめたクラスです。<br>
 * This class contains the logics to obtain/add/update the information of the address book sample application by web API.
 * <pre>
 * 本クラスでは、アドレス帳を操作する以下のメソッド、
 *   ・アドレス帳エントリリストの取得 (getEntryList())
 *   ・アドレス帳エントリ情報の取得 (getEntryInfo())
 *   ・アドレス帳エントリ情報の更新 (updateEntryInfo())
 *   ・アドレス帳エントリ情報の新規追加 (createEntry())
 *   ・アドレス帳エントリのユーザカウンタ情報の取得 (getUserCounter())
 *   ・アドレス帳エントリのエコカウンタ情報の取得 (getEcoCounter())
 *
 * 及び、一度の応答で全てのアドレス帳情報を取得しきれなかった場合に利用する、
 *   ・アドレス帳の続きの取得 (getContinuationUserList())
 *
 * を提供しています。
 *
 * This class provides the following methods to operate the address book.
 *  - Obtain address book entry list (getEntryList())
 *  - Obtain address book entry information (getEntryInfo())
 *  - Update address book entry information (updateEntryInfo())
 *  - Add address book entry information (createEntry())
 *  - Obtain address book entry user counter information (getUserCounter())
 *  - Obtain address book entry eco counter information (getEcoCounter())
 * This class also provides the following method to be used for the case all address book information cannot be obtained at once in a response.
 *  - Obtain remaining address book information (getContinuationUserList())
 * </pre>
 */
public class AddressBookSampleLogic {

    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "logic:AddressBSL:";

	/**
	 * アプリケーションのプロダクトID <br>
	 * Application product ID
	 */
	private final String productId;

	public AddressBookSampleLogic(String productId) {
		this.productId = productId;
	}

	/**
	 * アドレス帳エントリリストを取得します。<br>
	 * Obtains the address book entry list.
	 * <pre>
	 * 1. WebAPI実行のための Request を作成します。
	 * 2. クライアントクラスを利用して、作成した Request を実行します。
	 * 3. 実行応答 (response) から情報を取得します。
	 *
	 * エントリリスト取得では、
	 * 1-4 でエントリリスト検索条件を Query として設定しています。
	 * 本サンプルアプリケーションでは、特別検索条件を指定していませんが、
	 * 実際のアプリケーションではこの部分に検索条件を指定することで特定のエントリのみ取得することができます。
	 *
	 * さらに、エントリリスト取得の WebAPI は https のみサポートとなるため、
	 * 2-1 にて https のコンテキストを生成しています。
	 *
	 * WebAPIアクセス成功時はエントリリスト応答が返りますが、
	 * WebAPIアクセス失敗時はnullが返りますので注意してください。
	 *
	 * 1. Creates the request to run the web API.
	 * 2. Runs the created request by using the client class.
	 * 3. Obtains information from the response.
	 * The following processes are performed for obtaining entry list.
	 * In 1-4, the entry list search conditions are set as Query.
	 * This application does not specify specific search conditions.
	 * In the actual application, specific search conditions can be set to obtain certain entries.
	 * The web API to obtain entry list only supports https.
	 * In 2-1, the context for https is created.
	 * When WebAPI access is successful, the entry list is returned.
	 * However, when WebAPI access fails, null is returned.
	 * </pre>
	 *
	 * @return WebAPIアクセス成功時：エントリリスト応答<br>
	 *         WebAPIアクセス失敗時：null<br>
	 *         When WebAPI access is successful：Entry list<br>
	 *         When WebAPI access fails：null
	 */
	public GetEntryListResponseBody getEntryList() {
		// 1
		// 1-1
		final RequestHeader reqHeader = getRequestHeader();
		// 1-2
		final Request request = new Request();
		// 1-3
		request.setHeader(reqHeader);
		// 1-4
		final RequestQuery reqQuery = new RequestQuery();
		request.setQuery(reqQuery);

		// 2
		// 2-1
		final BasicRestContext context = new BasicRestContext("https");
		// 2-2
		final Addressbook addressbook = new Addressbook(context);
		Response<GetEntryListResponseBody> response = null;
		// 2-3
		try {
			response = addressbook.getEntryList(request);
			Log.d(Const.TAG, PREFIX +  response.getResponse().makeContentString("UTF-8"));

		} catch (InvalidResponseException ire) {
			logErrorResponse(ire);
			return null;
		} catch (IOException ioe) {
			logErrorResponse(ioe);
			return null;
		}

		// 3
		final GetEntryListResponseBody body = response.getBody();

		return body;
	}

	/**
	 * 取得したアドレス帳エントリリストに続きがある場合、このメソッドで続きを取得します。<br>
	 * アドレス帳ユーザリストに続きがある場合、応答属性内に nextLink 属性が含まれます。<br>
	 * この属性の値を取得し、本メソッドの nextLink 引数に指定して実行します。<br>
	 * If all address book entry list could not be obtained at once, the remaining entry list is obtained by this method.<br>
	 * If all address book entry list could not be obtained at once, the nextLink attribute is contained in the response attribute.<br>
	 * Obtain this attribute value and set to the nextLink argument for this method.
	 * <pre>
	 * 1. WebAPI実行のための Request を作成します。
	 * 2. クライアントクラスを利用して、作成した Request を実行します。
	 * 3. 実行応答 (response) から情報を取得します。
	 *
	 * エントリリスト取得の WebAPI は https のみサポートとなるため、
	 * 2-1 にて https のコンテキストを生成しています。
	 *
	 * WebAPIアクセス成功時はエントリリスト応答の続きが返りますが、
	 * WebAPIアクセス失敗時はnullが返りますので注意してください。
	 *
	 * 1. Creates the request to run the web API.
	 * 2. Runs the created request by using the client class.
	 * 3. Obtains the information from the response.
	 * The web API to obtain entry list only supports https.
	 * In 2-1, the context for https is created.
	 * When WebAPI access is successful, the remaining entry list response is returned.
	 * However, when WebAPI access fails, null is returned.
	 * </pre>
	 *
	 * @param nextLink エントリリスト応答の nextLink 属性<br>
	 *                  Entry list response nextLink attribute
	 * @return WebAPIアクセス成功時：エントリリスト応答の続き<br>
	 *         WebAPIアクセス失敗時：null<br>
	 *         When WebAPI access is successful：The remaining entry list response<br>
	 *         When WebAPI access fails：null.
	 */
	public GetEntryListResponseBody getContinuationEntryList(String nextLink) {
		// 1
		// 1-1
		final RequestHeader reqHeader = getRequestHeader();
		// 1-2
		final Request request = new Request();
		// 1-3
		request.setHeader(reqHeader);

		// 2
		// 2-1
		final BasicRestContext context = new BasicRestContext("https");
		// 2-2
		final Addressbook addressbook = new Addressbook(context);
		Response<GetEntryListResponseBody> response = null;
		// 2-3
		try {
			response = addressbook.getEntryListResponseContinuation(request, nextLink);
		} catch (InvalidResponseException ire) {
			logErrorResponse(ire);
			return null;
		} catch (IOException ioe) {
			logErrorResponse(ioe);
			return null;
		}

		// 3
		final GetEntryListResponseBody body = response.getBody();

		return body;
	}

	/**
	 * 指定したエントリID のアドレス帳エントリ情報を取得します。<br>
	 * Obtains the address book entry information of the specified entry ID.
	 * <pre>
	 * 1. WebAPI実行のための Request を作成します。
	 * 2. クライアントクラスを利用して、作成した Request を実行します。
	 * 3. 実行応答 (response) から情報を取得します。
	 *
	 * エントリ取得の WebAPI は https のみサポートとなるため、
	 * 2-1 にて https のコンテキストを生成しています。
	 *
	 * WebAPIアクセス成功時は取得したアドレス帳ユーザ情報が返りますが、
	 * WebAPIアクセス失敗時はnullが返りますので注意してください。
	 *
	 * 1. Creates the request to run the web API.
	 * 2. Runs the created request by using the client class.
	 * 3. Obtains the information from the response.
	 * The web API to obtain entry list only supports https.
	 * In 2-1, the context for https is created.
	 * When WebAPI access is successful, the obtained address book user information is returned.
	 * However, when WebAPI access fails, null is returned.
	 * </pre>
	 *
	 * @param entryId 取得対象のエントリID<br>
	 *                Entry ID to be obtained
	 * @return WebAPIアクセス成功時：取得したアドレス帳ユーザ情報<br>
	 *         WebAPIアクセス失敗時：null<br>
	 *         When WebAPI access is successful：Obtained address book user information<br>
	 *         When WebAPI access fails：null
	 */
	public GetEntryResponseBody getEntryInfo(String entryId) {
		// 1
		// 1-1
		final RequestHeader reqHeader = getRequestHeader();
		// 1-2
		final Request request = new Request();
		// 1-3
		request.setHeader(reqHeader);

		// 2
		// 2-1
		final BasicRestContext context = new BasicRestContext("https");
		// 2-2
		final Addressbook addressbook = new Addressbook(context);
		Response<GetEntryResponseBody> response = null;
		// 2-3
		try {
			response = addressbook.getEntry(request, entryId);
		} catch (InvalidResponseException ire) {
			logErrorResponse(ire);
			return null;
		} catch (IOException ioe) {
			logErrorResponse(ioe);
			return null;
		}

		// 3
		final GetEntryResponseBody body = response.getBody();

		return body;

	}

	/**
	 * 指定したエントリID のアドレス帳エントリ情報を指定した情報で更新します。<br>
	 * Updates the address book entry information of specified entry ID with specified information.
	 * <pre>
	 * 1. WebAPI実行のための Request を作成します。
	 * 2. クライアントクラスを利用して、作成した Request を実行します。
	 * 3. 実行応答 (response) から情報を取得します。
	 *
	 * エントリ情報更新では、更新情報を RequestBody に設定して要求を行います。
	 * 本サンプルアプリケーションでは、1-4 で引数に指定された更新情報を設定しています。
	 *
	 * エントリ情報更新の WebAPI は https のみサポートとなるため、
	 * 2-1 にて https のコンテキストを生成しています。
	 *
	 * WebAPIアクセス成功時は更新後のアドレス帳ユーザ情報が返りますが、
	 * WebAPIアクセス失敗時はnullが返りますので注意してください。
	 *
	 * 1. Creates the request to run the web API.
	 * 2. Runs the created request by using the client class.
	 * 3. Obtains the information from the response.
	 * When updating an entry information, the update information is set to RequestBody.
	 * In this sample application, the update information set as the argument in 1-4 is set.
	 * The web API to obtain entry list only supports https.
	 * In 2-1, the context for https is created.
	 * When WebAPI access is successful, the updated address book user information is returned.
	 * However, when WebAPI access fails, null is returned.
	 * </pre>
	 *
	 * @param updateInfo 更新情報<br>
	 *                   Update information
	 * @param entryId 更新対象のエントリID<br>
	 *                The entry ID to be updated
	 * @return WebAPIアクセス成功時：更新後のアドレス帳ユーザ情報<br>
	 *         WebAPIアクセス失敗時：null<br>
	 *         When WebAPI access is successful：Updated address book user information<br>
	 *         When WebAPI access fails：null
	 */
	public UpdateEntryResponseBody updateEntryInfo(UpdateEntryRequestBody updateInfo, String entryId) {
		// 1
		// 1-1
		final RequestHeader reqHeader = getRequestHeader();
		// 1-2
		final Request request = new Request();
		// 1-3
		request.setHeader(reqHeader);
		// 1-4
		request.setBody(updateInfo);

		// 2
		// 2-1
		final BasicRestContext context = new BasicRestContext("https");
		// 2-2
		final Addressbook addressbook = new Addressbook(context);
		Response<UpdateEntryResponseBody> response = null;
		// 2-3
		try {
			response = addressbook.updateEntry(request, entryId);
		} catch (InvalidResponseException ire) {
			logErrorResponse(ire);
			return null;
		} catch (IOException ioe) {
			logErrorResponse(ioe);
			return null;
		}

		// 3
		final UpdateEntryResponseBody body = response.getBody();

		return body;
	}

	/**
	 * アドレス帳エントリ情報新規作成します。<br>
	 * Creates new address book entry information.
	 * <pre>
	 * 1. WebAPI実行のための Request を作成します。
	 * 2. クライアントクラスを利用して、作成した Request を実行します。
	 * 3. 実行応答 (response) から情報を取得します。
	 *
	 * 新規エントリ作成は、新規エントリ情報を RequestBody に設定して要求を行います。
	 * 本サンプルアプリケーションでは、1-4 で引数に指定された新規エントリ情報を設定しています。
	 *
	 * 新規エントリ作成の WebAPI は https のみサポートとなるため、
	 * 2-1 にて https のコンテキストを生成しています。
	 *
	 * WebAPIアクセス成功時は追加されたアドレス帳エントリ情報が返りますが、
	 * WebAPIアクセス失敗時はnullが返りますので注意してください。
	 *
	 * 1. Creates the request to run the web API.
	 * 2. Runs the created request by using the client class.
	 * 3. Obtains the information from the response.
	 * When creating a new entry, the new entry information is set to RequestBody.
	 * In this sample application, the new entry information set as the argument in 1-4 is set.
	 * The web API to obtain entry list only supports https.
	 * In 2-1, the context for https is created.
	 * When WebAPI access is successful, the added address book entry information is returned.
	 * However, when WebAPI access fails, null is returned.
	 * </pre>
	 *
	 * @param createInfo 新規エントリ情報<br>
	 *                   New entry information
	 * @return WebAPIアクセス成功時：追加されたアドレス帳エントリ情報<br>
	 *         WebAPIアクセス失敗時：null<br>
	 *         When WebAPI access is successful：Added address book entry information<br>
	 *         When WebAPI access fails：null
	 */
	public CreateEntryResponseBody createEntry(CreateEntryRequestBody createInfo) {

		// 1
		// 1-1
		final RequestHeader reqHeader = getRequestHeader();
		// 1-2
		final Request request = new Request();
		// 1-3
		request.setHeader(reqHeader);
		// 1-4
		request.setBody(createInfo);

		// 2
		// 2-1
		final BasicRestContext context = new BasicRestContext("https");
		// 2-2
		final Addressbook addressbook = new Addressbook(context);
		Response<CreateEntryResponseBody> response = null;
		// 2-3
		try {
			response = addressbook.createEntry(request);
		} catch (InvalidResponseException ire) {
			logErrorResponse(ire);
			return null;
		} catch (IOException ioe) {
			logErrorResponse(ioe);
			return null;
		}

		// 3
		final CreateEntryResponseBody body = response.getBody();

		return body;
	}

	/**
	 * アドレス帳エントリ情報を削除します。<br>
	 * Deletes address book entry information.
	 * <pre>
	 * 1. WebAPI実行のための Request を作成します。
	 * 2. クライアントクラスを利用して、作成した Request を実行します。
	 * 3. 実行応答 (response) から情報を取得します。
	 *
	 * エントリ削除の WebAPI は https のみサポートとなるため、
	 * 2-1 にて https のコンテキストを生成しています。
	 *
	 * WebAPIアクセス成功時はEmptyResponseBody(空のリクエストボディを示すクラス)が返りますが、
	 * WebAPIアクセス失敗時はnullが返りますので注意してください。
	 *
	 * 1. Creates the request to run the web API.
	 * 2. Runs the created request by using the client class.
	 * 3. Obtains the information from the response.
	 * The web API to obtain entry list only supports https.
	 * In 2-1, the context for https is created.
	 * When WebAPI access is successful, the EmptyResponseBody (Class indicating the empty request body) is returned.
	 * However, when WebAPI access fails, null is returned.
	 * </pre>
	 *
	 * @param entryId 削除対象のエントリID<br>
	 *                Entry ID targeted to be deleted
	 * @return WebAPIアクセス成功時：EmptyResponseBody(空のリクエストボディを示すクラス)<br>
	 *         WebAPIアクセス失敗時：null<br>
	 *         When WebAPI access is successful：EmptyResponseBody (Class indicating the empty request body)<br>
	 *         When WebAPI access fails：null
	 */
	public EmptyResponseBody deleteEntry(String entryId) {

		// 1
		// 1-1
		final RequestHeader reqHeader = getRequestHeader();
		// 1-2
		final Request request = new Request();
		// 1-3
		request.setHeader(reqHeader);

		// 2
		// 2-1
		final BasicRestContext context = new BasicRestContext("https");
		// 2-2
		final Addressbook addressbook = new Addressbook(context);
		Response<EmptyResponseBody> response = null;
		// 2-3
		try {
			response = addressbook.deleteEntry(request, entryId);
		} catch (InvalidResponseException ire) {
			logErrorResponse(ire);
			return null;
		} catch (IOException ioe) {
			logErrorResponse(ioe);
			return null;
		}

		// 3
		final EmptyResponseBody body = response.getBody();

		return body;
	}

	/**
	 * 指定したユーザーコードのユーザのユーザーカウンタ情報を取得します。<br>
	 * Obtains the user counter information of the specified user code.
	 * <pre>
	 * 1. WebAPI実行のための Request を作成します。
	 * 2. クライアントクラスを利用して、作成した Request を実行します。
	 * 3. 実行応答 (response) から情報を取得します。
	 *
	 * ジョブログ取得の WebAPI は https のみサポートとなるため、
	 * 2-1 にて https のコンテキストを生成しています。
	 *
	 * WebAPIアクセス成功時はユーザーカウンタ情報が返りますが、
	 * WebAPIアクセス失敗時はnullが返りますので注意してください。
	 *
	 * 1. Creates the request to run the web API.
	 * 2. Runs the created request by using the client class.
	 * 3. Obtains the information from the response.
	 * The web API to obtain entry list only supports https.
	 * In 2-1, the context for https is created.
	 * When WebAPI access is successful, the user counter information is returned.
	 * However, when WebAPI access fails, null is returned.
	 * </pre>
	 *
	 * @param usercode 対象ユーザのユーザコード<br>
	 *                 User code of the target user
	 * @return WebAPIアクセス成功時：ユーザーカウンタ情報<br>
	 *         WebAPIアクセス失敗時：null<br>
	 *         When WebAPI access is successful：User counter information<br>
	 *         When WebAPI access fails：null
	 */
	public GetUserCounterResponseBody getUserCounter(String usercode) {
		// 1
		// 1-1
		final RequestHeader reqHeader = getRequestHeader();
		// 1-2
		final Request request = new Request();
		// 1-3
		request.setHeader(reqHeader);

		// 2
		// 2-1
		final BasicRestContext context = new BasicRestContext("https");
		// 2-2
		final Counter counter = new Counter(context);
		Response<GetUserCounterResponseBody> response = null;
		// 2-3
		try {
			response = counter.getUserCounter(request, usercode);
		} catch (InvalidResponseException ire) {
			logErrorResponse(ire);
			return null;
		} catch (IOException ioe) {
			logErrorResponse(ioe);
			return null;
		}

		// 3
		final GetUserCounterResponseBody body = response.getBody();

		return body;
	}

	/**
	 * 指定したユーザーコードのユーザのエコカウンタ情報を取得します。<br>
	 * Obtains the eco counter information of the specified user code.
	 * <pre>
	 * 1. WebAPI実行のための Request を作成します。
	 * 2. クライアントクラスを利用して、作成した Request を実行します。
	 * 3. 実行応答 (response) から情報を取得します。
	 *
	 * ジョブログ取得の WebAPI は https のみサポートとなるため、
	 * 2-1 にて https のコンテキストを生成しています。
	 *
	 * WebAPIアクセス成功時はエコカウンタ情報が返りますが、
	 * WebAPIアクセス失敗時はnullが返りますので注意してください。
	 *
	 * 1. Creates the request to run the web API.
	 * 2. Runs the created request by using the client class.
	 * 3. Obtains the information from the response.
	 * The web API to obtain entry list only supports https.
	 * In 2-1, the context for https is created.
	 * When WebAPI access is successful, the eco counter information is returned.
	 * However, when WebAPI access fails, null is returned.
	 * </pre>
	 *
	 * @param usercode 対象ユーザのユーザコード<br>
	 *                 User code of the target user
	 * @return WebAPIアクセス成功時：エコカウンタ情報<br>
	 *         WebAPIアクセス失敗時：null<br>
	 *         When WebAPI access is successful：Eco counter information<br>
	 *         When WebAPI access fails：null
	 */
	public GetEcoCounterResponseBody getEcoCounter(String usercode) {
		// 1
		// 1-1
		final RequestHeader reqHeader = getRequestHeader();
		// 1-2
		final Request request = new Request();
		// 1-3
		request.setHeader(reqHeader);

		// 2
		// 2-1
		final BasicRestContext context = new BasicRestContext("https");
		// 2-2
		final Counter counter = new Counter(context);
		Response<GetEcoCounterResponseBody> response = null;
		// 2-3
		try {
			response = counter.getEcoCounter(request, usercode);
		} catch (InvalidResponseException ire) {
			logErrorResponse(ire);
			return null;
		} catch (IOException ioe) {
			logErrorResponse(ioe);
			return null;
		}

		// 3
		final GetEcoCounterResponseBody body = response.getBody();

		return body;
	}

	/**
	 * WebAPI実行のためのヘッダーを取得します。<br>
	 * WebAPI実行のヘッダーにはアプリケーションのプロダクトID 属性を設定する必要があるため、<br>
	 * ここで共通化しています。<br>
	 * Obtains the header to run the web API.<br>
	 * For the header to run the web API, the application product ID attribute must be set.<br>
	 * Set as common header.
	 *
	 * @return API実行のためのヘッダー<br>
	 *         The header to run the web API.
	 */
	private RequestHeader getRequestHeader() {
		final RequestHeader header = new RequestHeader();
		// 要求元アプリID を設定します。
		// Sets the requester application ID.
		header.put(RequestHeader.KEY_X_APPLICATION_ID, productId);

		return header;
	}

	/**
	 * WebAPI の実行エラーをログ出力します。<br>
	 * Outputs the web API error in log.
	 * @param e 例外<br>
	 *          Exception
	 */
	private void logErrorResponse(Exception e) {

		if (e instanceof InvalidResponseException) {
			final InvalidResponseException ire = (InvalidResponseException)e;
			Log.e(Const.TAG, PREFIX + ire.toString());
			final StringBuffer buf = new StringBuffer();
			buf.append("Request failed.").append("\n");
			buf.append("  ").append("Error code = ").append(ire.getStatusCode()).append("\n");
			if (ire.hasBody()) {
				final ErrorResponseBody errorBody = ire.getBody();
				final ErrorsArray errorsArray = errorBody.getErrors();
				for (Iterator<Errors> itr = errorsArray.iterator();  itr.hasNext(); ) {
					final Errors errors = itr.next();
					buf.append("    ").append(errors.getMessageId()).append(" : ").append(errors.getMessage()).append("\n");
				}
			}
			Log.e(Const.TAG, PREFIX + buf.toString());
		}
		else {
			Log.e(Const.TAG, PREFIX + e.toString());
		}

	}

}
