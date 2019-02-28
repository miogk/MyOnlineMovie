package com.example.miogk.myonlinemovie;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.miogk.myonlinemovie.utilssssss.MySqliteDatabaseHelper;
import com.example.miogk.myonlinemovie.utilssssss.MyUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.login_layout_username)
    TextView login_layout_username;
    @BindView(R.id.login_layout_password)
    TextView login_layout_password;
    @BindView(R.id.login_layout_login)
    Button login_layout_login;
    @BindView(R.id.login_layout_submit)
    Button login_layout_submit;

    private boolean singleTop;
    private MySqliteDatabaseHelper sqLiteOpenHelper;
    Handler handler = new Handler();
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        getWindow().setStatusBarColor(getResources().getColor(R.color.pink));
        Intent intent = getIntent();
        sqLiteOpenHelper = new MySqliteDatabaseHelper(this);
        database = sqLiteOpenHelper.getWritableDatabase();
        singleTop = intent.getBooleanExtra("singleTop", false);
        if (singleTop) {
            login_layout_login.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = login_layout_submit.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        login_layout_submit.setOnClickListener(this);
        login_layout_login.setOnClickListener(this);
    }

    private int invalide() {
        if (TextUtils.isEmpty(login_layout_username.getText().toString()) || login_layout_username.getText().length() > 8) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("用户名为空或用户名大于8位数，请重新输入!");
            builder.show();
            EventBus.getDefault().post("");
            return -1;
        }
        if (TextUtils.isEmpty(login_layout_password.getText().toString()) || login_layout_password.getText().length() > 6) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("密码为空或用户名大于6位数，请重新输入!");
            builder.show();
            return -1;
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_layout_submit:
                if (!singleTop) {
                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                    intent.putExtra("singleTop", true);
                    startActivity(intent);
                    finish();
                } else {
                    if (invalide() == 0) {
                        Cursor cursor = database.rawQuery("select username from user where username = ?", new String[]{login_layout_username.getText().toString()});
                        if (cursor.moveToNext()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                                    .setMessage("已经有此用户名");
                            builder.show();
                            break;
                        } else {
                            database.execSQL("insert into user (username, password) values (?,?)", new String[]{login_layout_username.getText().toString(),
                                    login_layout_password.getText().toString()});
                            MyUtils.putInSharedPreferences(LoginActivity.this, "user", "username", login_layout_username.getText().toString());
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("注册成功");
                            final AlertDialog alertDialog = builder.show();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertDialog.cancel();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
//                                    finish();
                                }
                            }, 1500);
                        }
                    } else {
                        break;
                    }
                }
                break;
            case R.id.login_layout_login:
                if (invalide() == 0) {
                    //先判断是否有此用户名
                    String sql = "select id from user where username = ?";
                    Cursor cursor = database.rawQuery(sql, new String[]{login_layout_username.getText().toString()});
                    try {
                        if (cursor.moveToNext()) {
                            Cursor password_cursor = null;
                            try {
                                String password_sql = "select password from user where username = ?";
                                password_cursor = database.rawQuery(password_sql, new String[]{login_layout_username.getText().toString()});
                                String password_result = "";
                                //用户名正确的情况下再判断密码
                                if (password_cursor.moveToNext()) {
                                    password_result = password_cursor.getString(password_cursor.getColumnIndex("password"));
                                }
                                //正确了就SharedPreferences里存一份username
                                if (password_result.equals(login_layout_password.getText().toString())) {
                                    MyUtils.putInSharedPreferences(LoginActivity.this, "user", "username", login_layout_username.getText().toString());
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("登录成功");
                                    final AlertDialog alertDialog = builder.show();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            alertDialog.cancel();
                                            //跳转到主页
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
//                                            finish();
                                        }
                                    }, 1500);
                                    //密码错误就用dialog提示
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("密码错误");
                                    builder.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (password_cursor != null) {
                                    password_cursor.close();
                                }
                            }
                            //用户名错误
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("用户名错误");
                            builder.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                } else {
                    return;
                }
                break;
        }
    }
}