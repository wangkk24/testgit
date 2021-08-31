package com.pukka.ydepg.moudule.livetv.presenter.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: BaseView
 * @Package com.pukka.ydepg.moudule.livetv.presenter.base
 * @date 2017/12/26 10:07
 */
public interface BaseView{
  <Z> LifecycleTransformer<Z> bindToLife();
}