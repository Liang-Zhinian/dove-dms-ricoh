/*
 *  Copyright (C) 2013-2014 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.app.scan.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.dove.R;
import com.dove.sample.app.scan.application.ScanSampleApplication;
import com.dove.sample.function.scan.ScanJob;
import com.dove.sample.function.scan.ScanThumbnail;
import com.dove.sample.function.scan.attribute.standard.ScanJobScanningInfo;
import com.dove.sample.wrapper.common.BinaryResponseBody;
import com.dove.sample.wrapper.common.Response;

/**
 * プレビュー画像を表示するアクティビティです。
 * The activity to display preview screen.
 */
public class PreviewActivity extends Activity{

    /**
     * このアクティビティのリクエストコード
     * Request code of preview activity
     */
    public static final int REQUEST_CODE_PREVIEW_ACTIVITY = 2000;

    /**
     * サムネイルの総数
     * Total number of thumbnails.
     */
    private int mTotalPage;

    /**
     * 表示中のサムネイルのページ番号
     * Page number of the currently displayed thumbnail.
     */
    private int mCurrentPage;

    /**
     * 総ページ数のラベル
     * The text label to indicate the total page
     */
    private TextView mTotalPageNoView;

    /**
     * 現在のページ数のラベル
     * The text label to indicate the current page
     */
    private TextView mCurrentPageNoView;

    /**
     * 次のページへボタン
     * Next page button
     */
    private View mNextPageArrow;

    /**
     * 前のページへボタン
     * Previout page button
     */
    private View mPrevPageArrow;

    /**
     * プログレスバー
     * Progress bar
     */
    private ProgressBar mProgressBar;

    /**
     * サムネイル画像のイメージビュー
     * Thumbnail image view
     */
    private ImageView mPreviewImage;

    private int mPreviewImageWidth = 0;
    private int mPreviewImageHeight = 0;

    /**
     * サムネイル画像オブジェクト
     * Thumnail image object
     */
    private ScanThumbnail mThumbnail;

    /**
     * サムネイル画像を読み込むタスク
     * The task to load thumbnail image
     */
    private LoadThumbnailImageTask mLoaderTask;


    /**
     * アクティビティが生成されると呼び出されます。
     * [処理内容]
     *   (1)参照の設定
     *   (2)総ページ数取得タスクの開始
     *   (3)キャンセルボタンの設定
     *   (4)送信ボタンの設定
     *   (5)前のページへボタンの設定
     *   (6)次のページへボタンの設定
     *
     * Called when an activity is created.
     * [Processes]
     *  (1) Set reference
     *  (2) Execute the task to obtain the total number of pages
     *  (3) Set cancel button
     *  (4) Set send button
     *  (5) Set next page button
     *  (6) Set previous page button
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        //(1)
        ScanSampleApplication app = (ScanSampleApplication) getApplication();
        mThumbnail = new ScanThumbnail(app.getScanJob());
        mTotalPageNoView = (TextView) findViewById(R.id.text_total_page);
        mCurrentPageNoView = (TextView) findViewById(R.id.text_cur_page);
        mNextPageArrow = findViewById(R.id.btn_page_next);
        mPrevPageArrow = findViewById(R.id.btn_page_prev);

        mProgressBar = (ProgressBar) findViewById(R.id.preview_proc_bar);
        mProgressBar.setVisibility(View.INVISIBLE);
        mPreviewImage = (ImageView) findViewById(R.id.image_preview);
        mPreviewImage.setScaleType(ScaleType.MATRIX);

        //(2)
        mTotalPage = 1;
        mCurrentPage = 1;
        new GetTotalPageTask().execute(app.getScanJob());

        //(3)
        Button btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        //(4)
        Button btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        //(5)
        findViewById(R.id.btn_page_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPage >= mTotalPage) {
                    return;
                }
                mCurrentPage++;
                updatePageNavigator();

                if (mLoaderTask != null) {
                    mLoaderTask.cancel(false);
                }
                mLoaderTask = new LoadThumbnailImageTask();
                mLoaderTask.execute(mCurrentPage);
            };
        });

        //(6)
        findViewById(R.id.btn_page_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPage <= 1) {
                    return;
                }
                mCurrentPage--;
                updatePageNavigator();

                if (mLoaderTask != null) {
                    mLoaderTask.cancel(false);
                }
                mLoaderTask = new LoadThumbnailImageTask();
                mLoaderTask.execute(mCurrentPage);
            };
        });

    }

    /**
     * アクティビティの再開時に呼び出されます。
     * Called when the activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(DialogUtil.INTENT_ACTION_SUB_ACTIVITY_RESUMED));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        synchronized (this) {
            mPreviewImageWidth = mPreviewImage.getWidth();
            mPreviewImageHeight = mPreviewImage.getHeight();
            notifyAll();
        }
    }

    /**
     * ページ番号、次ページ・前ページ遷移用の矢印を更新します。
     * Updates the page labels and navigator images.
     */
    private void updatePageNavigator() {
        mCurrentPageNoView.setText(Integer.toString(mCurrentPage));
        mTotalPageNoView.setText(Integer.toString(mTotalPage));
        mNextPageArrow.setVisibility((mCurrentPage < mTotalPage)? View.VISIBLE : View.INVISIBLE);
        mPrevPageArrow.setVisibility((mCurrentPage > 1)? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 読み取り枚数を取得して画面を更新する非同期タスクです。
     * The asynchronous task to obtain the total number of pages and update the screen.
     */
    private class GetTotalPageTask extends AsyncTask<ScanJob, Void, Integer> {

        @Override
        protected Integer doInBackground(ScanJob... params) {
            ScanJob scanJob = params[0];
            ScanJobScanningInfo scanningInfo = (ScanJobScanningInfo) scanJob.getJobAttribute(ScanJobScanningInfo.class);
            if (scanningInfo == null) {
                return 1;
            }
            return scanningInfo.getScannedCount();
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (PreviewActivity.this.isFinishing()) {
                return;
            }

            mTotalPage = result;
            updatePageNavigator();

            mLoaderTask = new LoadThumbnailImageTask();
            mLoaderTask.execute(mCurrentPage);
        }

    }

    /**
     * プレビュー画像データを保持するクラスです。
     * This class stores preview image data.
     */
    private static class ThumbnailImageData {

        /**
         * 画像バイナリデータ
         * Image binary data
         */
        byte[] bytes;

        /**
         * 正立表示のための回転角
         * Rotation angle to display the image in correct direction
         */
        String xRotate;

        /**
         * バイナリデータをロードしたビットマップ画像
         * Bitmap image in which binary data is loaded
         */
        Bitmap bitmap;
    }

    /**
     * 指定されたページのプレビュー画像を取得・表示する非同期タスクです。
     * 処理中はプログレスバーを表示します。
     * サムネイル画像をバイト配列形式で取得します。
     * 画像サイズからImageViewに収まる比率を求めた後に、画像をロードします。
     *
     * The asynchronous task to obtain and display the preview image of the specified page.
     * During the process, displays a progress bar.
     * Obtains the thumbnail image in byte array.
     * Obtains the ratio to fit within ImageView from the image size and then loads the image.
     *
     */
    private class LoadThumbnailImageTask extends AsyncTask<Integer, Void, ThumbnailImageData> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ThumbnailImageData doInBackground(Integer... params) {
            ThumbnailImageData data = getThumbnailImageData(params[0]);
            if (data.bytes == null) {
                return data;
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data.bytes, 0, data.bytes.length, options);

            float scaleW = (float)options.outWidth / getPreviewImageWidth();
            float scaleH = (float)options.outHeight / getPreviewImageHeight();
            int scale = (int) Math.ceil(Math.max(scaleW,  scaleH));

            options.inJustDecodeBounds = false;
            options.inSampleSize = calculateSampleSize(scale);

            data.bitmap = BitmapFactory.decodeByteArray(data.bytes, 0, data.bytes.length, options);
            return data;
        }

        private int getPreviewImageWidth() {
            synchronized (PreviewActivity.this) {
                while (mPreviewImageWidth == 0) {
                    try {
                        PreviewActivity.this.wait(1000);
                    } catch (InterruptedException e) {
                    }
                }
                return mPreviewImageWidth;
            }
        }
        private int getPreviewImageHeight() {
            synchronized (PreviewActivity.this) {
                while (mPreviewImageHeight == 0) {
                    try {
                        PreviewActivity.this.wait(1000);
                    } catch (InterruptedException e) {
                    }
                }
                return mPreviewImageHeight;
            }
        }

        /**
         * 指定されたページのプレビュー画像バイナリデータを取得します。
         * Obtains the binary data of the preview image of a specified page.
         *
         * @param pageNo 取得するページ番号
         *               Number of the page to obtain
         * @return
         */
        private ThumbnailImageData getThumbnailImageData(int pageNo) {
            ThumbnailImageData data = new ThumbnailImageData();

            Response<BinaryResponseBody> response = mThumbnail.getThumbnailBinaryResponse(pageNo);
            if (response != null) {
                data.bytes = response.getBody().toByteArray();
                data.xRotate = response.getAllHeaders().get("X-Rotate");
            }
            return data;
        }

        /**
         * BitmapFactory.Options.inSampleSize の設定値を計算します。
         * 引数で渡された値以上で一番近い2のべき乗を返却します。
         * Calculates the setting value of BitmapFactory.Options.inSampleSize.
         * Returns the nearest exponentiation of 2 that is equal to or larger than the value passed by the argument.
         */
        private int calculateSampleSize(int scale) {
            int result = scale;
            for (int i = 1; ; i*=2) {
                if (i >= scale) {
                    result = i;
                    break;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(ThumbnailImageData result) {
            if (PreviewActivity.this.isFinishing()) {
                return;
            }

            if (! isCancelled()) {
                mPreviewImage.setImageBitmap(null);

                if (result.bitmap != null) {
                    mPreviewImage.setImageBitmap(result.bitmap);

                    // Matrixクラスを使用して画像正立表示のための回転角を設定します
                    // Sets the rotation angle to display the image in correct direction using Matrix class.
                    int rotate = 0;
                    if (result.xRotate != null) {
                        try {
                            rotate = Integer.parseInt(result.xRotate);
                        } catch (NumberFormatException e) {
                            rotate = 0;
                        }
                    }
                    Matrix matrix = new Matrix();
                    matrix.preTranslate(-result.bitmap.getWidth() / 2, -result.bitmap.getHeight() / 2);
                    matrix.postRotate(rotate);
                    matrix.postTranslate(mPreviewImage.getWidth() / 2, mPreviewImage.getHeight() / 2);
                    mPreviewImage.setImageMatrix(matrix);
                    mPreviewImage.invalidate();
                }
            }
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onCancelled() {
            mProgressBar.setVisibility(View.INVISIBLE);
        }

    }

}
