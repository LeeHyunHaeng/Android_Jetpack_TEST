package org.techtown.navigation_test.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.techtown.navigation_test.DataModel.NoteList
import org.techtown.navigation_test.DataModel.NoteViewModel
import org.techtown.navigation_test.DataModel.SetEvent
import org.techtown.navigation_test.MainActivity
import org.techtown.navigation_test.R
import org.techtown.navigation_test.adpater.NoteAdapter
import org.techtown.navigation_test.custom_dialog.CustomDialog
import org.techtown.navigation_test.databinding.FragmentMainBinding

class FragmentMain : Fragment(), View.OnClickListener {

    private val TAG: String = "FragmentMain"

    private var mBinding: FragmentMainBinding? = null
    private val binding get() = mBinding!!

    private lateinit var navController: NavController
    private lateinit var mContext: MainActivity
    private lateinit var mAdapter: NoteAdapter
    private lateinit var mList: ArrayList<NoteList>
    private lateinit var mViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.mainPlus.setOnClickListener(this)
        binding.list.setOnClickListener(this)

        binding.mainRecyclerview.apply {
            mList = ArrayList<NoteList>()
            mAdapter = NoteAdapter(mContext, mList)
            mAdapter.notifyDataSetChanged()
            layoutManager = LinearLayoutManager(mContext)
            adapter = mAdapter
        }

        mViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        //옵저버로 현재값을 관찰하여 바뀔때마다 할당한다.
        mViewModel.currentCount.observe(viewLifecycleOwner, Observer {
            binding.mainCount.text = it.toString()
            Log.d(TAG, "onViewCreated: mList.size가 변화였습니다 = ${mList.size}")
        })
    }

    //context 가져오기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            //list 클릭시 리스트화면으로 이동
            binding.list.id -> {
                navController.navigate(R.id.action_fragment_main_to_like_list)
            }
            //다이얼로그 생성 및 데이터 가져오기
            binding.mainPlus.id -> {
                val dialog = CustomDialog(mContext)
                dialog.showDialog()
                dialog.setContentsListener(object : CustomDialog.sendContentsListener {
                    override fun addContents(contents: String) {
                        mList.add(NoteList(contents))
                        if (!contents.isEmpty()) {
                            mViewModel.updateCount(event = SetEvent.PLUS)
                        }
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}


























