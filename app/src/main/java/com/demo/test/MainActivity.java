package com.demo.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.demo.test.adapter.RvAdapter;
import com.demo.test.util.GlideSimpleTarget;
import com.demo.test.util.StorageUtil;
import com.demo.test.util.Utils;
import com.github.ielse.imagewatcher.ImageWatcher;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements ImageWatcher.OnPictureLongPressListener, ImageWatcher.Loader {
    private RecyclerView mRecyclerView;
    private RvAdapter mRvAdapter;
    private List<String> mList;
    private List<List<String>> mLists = new ArrayList<>();
    ImageWatcher imageWatcher;
    private static MyHandler myHandler;
    private static String dstPath;
    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
        initAdapter();
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        imageWatcher = findViewById(R.id.imageWatcher);
        //初始化仿微信图片滑动加载器
        imageWatcher.setTranslucentStatus(Utils.calcStatusBarHeight(this));
        imageWatcher.setErrorImageRes(R.mipmap.error_picture);
        imageWatcher.setOnPictureLongPressListener(this);
        imageWatcher.setLoader(this);
        getPermissions();
        myHandler = new MyHandler(this);
    }

    private void initData() {
        mLists = getLists();
    }

    private void initAdapter() {
        mRvAdapter = new RvAdapter(this, mLists, imageWatcher);
        mRecyclerView.setAdapter(mRvAdapter);
    }

    private List<List<String>> getLists() {
        for (int i = 0; i < 5; i++) {
            mList = new ArrayList<>();
            mList.add("https://img.ivsky.com/img/tupian/li/202107/20/maitian-005.jpg");
            mList.add("https://img.ivsky.com/img/tupian/li/202107/16/shandi-008.jpg");
            mList.add("https://img.ivsky.com/img/tupian/li/202107/14/feiyi_laguan-008.jpg");
            mList.add("https://img.ivsky.com/img/tupian/li/202107/15/xingkong-005.jpg");
            mList.add("https://img.ivsky.com/img/tupian/li/202107/12/maitian-011.jpg");
            mList.add("https://img.ivsky.com/img/tupian/li/202107/13/xueshan-031.jpg");
            mList.add("https://img.ivsky.com/img/tupian/li/202107/15/xingkong-005.jpg");
            mList.add("https://img.ivsky.com/img/tupian/li/202107/16/shandi-008.jpg");
            mList.add("https://img.ivsky.com/img/tupian/li/202107/05/maitian-005.jpg");
            mLists.add(mList);
        }
        return mLists;
    }

    @Override
    public void load(Context context, Uri uri, ImageWatcher.LoadCallback loadCallback) {
        Glide.with(context).asBitmap().load(uri.toString()).into(new GlideSimpleTarget(loadCallback));
    }

    @Override
    public void onPictureLongPress(ImageView v, final Uri uri, int pos) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("保存图片");
        alert.setMessage("你确定要保存图片吗?");
        alert.setNegativeButton("取消", null);
        alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            if (uri != null) {// Always true pre-M
                                savePhoto(uri);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "缺少必要权限,请授予权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void getPermissions() {
        rxPermissions = new RxPermissions(this);
    }

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {
        private WeakReference<Activity> mActivity;
        private Bitmap bitmap;

        private MyHandler(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Activity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == 1) {
                    try {
                        bitmap = (Bitmap) msg.obj;
                        if (Utils.saveBitmap(bitmap, dstPath, false)) {
                            try {
                                ContentValues values = new ContentValues(2);
                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                values.put(MediaStore.Images.Media.DATA, dstPath);
                                getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                Toast.makeText(activity, "图片已保存到手机", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                Toast.makeText(activity, "图片保存失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(activity, "图片保存失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(activity, "图片保存失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /**
     * 长按保存图片
     *
     * @param uri 图片url地址
     */
    private void savePhoto(Uri uri) {
        Glide.with(MainActivity.this).asBitmap().load(uri).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                String picPath = StorageUtil.getSystemImagePath();
                StorageUtil.init(MainActivity.this, picPath);
                dstPath = picPath + (System.currentTimeMillis() / 1000) + ".jpeg";
                Message message = Message.obtain();
                message.what = 1;
                message.obj = resource;
                myHandler.sendMessage(message);
                return false;
            }

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                return false;
            }
        }).submit();
    }
}
