/*
 * Copyright (c) 2019. RRatChet Open Source Project.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 项目名称：rratchet-android-router-trunk
 * 模块名称：app
 *
 * 文件名称：ScrollingActivity.java
 * 文件描述：
 *
 * 创 建 人：ASLai(laijianhua@ruixiude.com)
 *
 * 上次修改时间：2019-07-25 10:53:23
 *
 * 修 改 人：ASLai(laijianhua@ruixiude.com)
 * 修改时间：2019-07-25 14:08:24
 * 修改备注：
 */

package com.rratchet.android.router.app;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rratchet.android.router.Router;
import com.rratchet.android.router.support.annotation.Requested;
import com.rratchet.android.router.support.annotation.Route;

@Route(path = "/scrolling")
public class ScrollingActivity extends AppCompatActivity {

    @Requested
    int id;

    @Requested
    int[] ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Router.inject(this);
    }
}
