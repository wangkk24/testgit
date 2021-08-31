/*
 *Copyright (C) 2018 广州易杰科技, Inc.
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package com.pukka.ydepg.moudule.mytv.adapter;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.pukka.ydepg.R;
import com.pukka.ydepg.moudule.livetv.adapter.BaseAdapter;
import com.pukka.ydepg.moudule.livetv.adapter.BaseHolder;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;

import java.util.List;

/**
 * 支付键盘adapter
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: PayKeyBoardAdapter
 * @Package com.pukka.ydepg.moudule.mytv.adapter
 * @date 2018/01/23 16:43
 */
public class PayKeyBoardAdapter extends BaseAdapter<String,PayKeyBoardAdapter.KeyBoardHolder> implements BaseAdapter.OnItemkeyListener {

  private View rl_delete;

  public PayKeyBoardAdapter(Context context, List<String> list, View rl_delete) {
    super(context, list);
    this.rl_delete=rl_delete;
    setOnItemKeyListener(this);
  }


  @Override protected KeyBoardHolder createViewHolder(View view) {
    return new KeyBoardHolder(view);
  }

  @Override protected int getAdapterLayoutId() {
    return R.layout.item_pay_keyboard;
  }

  @Override
  public boolean onItemkeyListener(int keyCode, KeyEvent event, int position,String viewType) {
    if (position==0&&null!=rl_delete&&KeyEvent.ACTION_DOWN == event.getAction()) {
      int codeValue = RemoteKeyEvent.getInstance().getVODKeyCodeValue(keyCode);
      if(codeValue == RemoteKeyEvent.VOD_FAST_REWIND&&null!=rl_delete){
        rl_delete.setFocusable(true);
        rl_delete.requestFocus();
        return true;
      }
    }

    return false;
  }

  public class KeyBoardHolder extends BaseHolder<String>{
    @BindView(R.id.tv_keyboard_item) TextView mItemKeyBoard;
    public KeyBoardHolder(View itemView) {
      super(itemView);
    }
    @Override public void bindView(String value, int position) {
      mItemKeyBoard.setText(value);
    }
  }
}
