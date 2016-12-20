package com.example.fiveyuanstore.page;

import java.io.Serializable;
import java.util.List;

import com.example.fiveyuanstore.GoodsContentActivity;
import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.fragment.widgets.PictureView;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class CommodityFragment extends Fragment{
	
	View view;
	ListView listView;
	
	List<Goods> data;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {//
		
		if(view==null){
			view=inflater.inflate(R.layout.fragment_page_commodity, null);
		
			listView=(ListView) view.findViewById(R.id.goods_list);
			listView.setAdapter(listAdapter);
		
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onItemClicked(position);
					
				}
			});
		
		}
		return view;
	}

	BaseAdapter listAdapter=new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view=null;
			if(convertView==null){
				LayoutInflater inflater=LayoutInflater.from(getActivity());			
				view=inflater.inflate(R.layout.fragment_inputcell_goods, null);
			}else{
				view=convertView;
			}
			TextView textContent = (TextView) view.findViewById(R.id.text);
			TextView goodsName = (TextView) view.findViewById(R.id.goods_name);
			
			TextView textDate = (TextView) view.findViewById(R.id.date);
			PictureView img = (PictureView) view.findViewById(R.id.goods_img);
			
			Goods goods=data.get(position);
			
			textContent.setText(goods.getText());
			goodsName.setText(goods.getTitle());
			img.load(goods);
			
			String dateStr=DateFormat.format("yyyy-MM-dd hh:mm",goods.getCreateDate()).toString();
			textDate.setText(dateStr);
			
			return view;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data==null?0:data.size();
		}
	};
	
	
	void onItemClicked(int position) {
		Goods pos = data.get(position);

		Intent itnt = new Intent(this.getActivity(), GoodsContentActivity.class);
		itnt.putExtra("pos", (Serializable) pos);
		startActivity(itnt);
	}
}
