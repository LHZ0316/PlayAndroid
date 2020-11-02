package com.zj.play.project.list

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.zj.model.pojo.QueryArticle
import com.zj.play.R
import com.zj.play.article.ArticleAdapter
import com.zj.play.base.BaseListFragment

private const val PROJECT_CID = "PROJECT_CID"

class ProjectListFragment : BaseListFragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(ProjectListViewModel::class.java) }

    private var projectCid: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            projectCid = it.getInt(PROJECT_CID)
        }
    }

    override fun refreshData() {
        getArticleList(true)
    }

    override fun isHaveHeadMargin(): Boolean {
        return false
    }

    override fun getLayoutId(): Int = R.layout.fragment_base_list

    override fun initView() {
        articleAdapter = ArticleAdapter(context!!, viewModel.dataList)
        super.initView()
    }

    private fun getArticleList(isRefresh: Boolean) {
        if (viewModel.dataList.size <= 0)
            startLoading()
        viewModel.getDataList(QueryArticle(page, projectCid!!, isRefresh))
    }

    override fun initData() {
        setDataStatus(viewModel.dataLiveData, {
            if (viewModel.dataList.size > 0) loadFinished()
        }) {
            if (page == 1 && viewModel.dataList.size > 0) {
                viewModel.dataList.clear()
            }
            viewModel.dataList.addAll(it)
            articleAdapter.notifyDataSetChanged()
        }
        getArticleList(false)
    }

    companion object {
        @JvmStatic
        fun newInstance(cid: Int) =
            ProjectListFragment().apply {
                arguments = Bundle().apply {
                    putInt(PROJECT_CID, cid)
                }
            }
    }

}
