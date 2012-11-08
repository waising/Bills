package com.bosssoft.bills.module.basic;

import com.bosssoft.bills.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;

/** 
 *
 * @ClassName   ������MenuActivity 
 * @Description ����˵����
 * TODO
 ************************************************************************
 * @date        �������ڣ�2012-11-8
 * @author      �����ˣ�wwx
 * @version     �汾�ţ�V1.0
 *<p>
 ***************************�޶���¼*************************************
 * 
 *   2012-11-8   wwx   �������๦�ܡ�
 *
 ************************************************************************
 *</p>
 */
public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
//		int men = Menu.FIRST;
//		menu.addSubMenu(men, men,men, "�˵�1");
//		menu.addSubMenu(men+1, men+1,men+1, "�˵�2");
//		menu.addSubMenu(men+2, men+2,men+2, "�˵�3");
//		menu.addSubMenu(men+3, men+3,men+3, "�˵�4");
//		menu.addSubMenu(men+4, men+4,men+4, "�˵�5");
//		menu.addSubMenu(men+5, men+5,men+5, "�˵�6");
//		menu.addSubMenu(men+6, men+6,men+6, "�˵�7");
//		menu.addSubMenu(men+7, men+7,men+7, "�˵�8");
//		menu.addSubMenu(men+8, men+8,men+8, "�˵�9");
//		menu.addSubMenu(men+9, men+9,men+9, "�˵�10");
		
		this.getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 1:
			Toast.makeText(getApplicationContext(), "menu1", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Toast.makeText(getApplicationContext(), "menu2", Toast.LENGTH_SHORT).show();
			break;
		case 3:
			Toast.makeText(getApplicationContext(), "menu3", Toast.LENGTH_SHORT).show();
			break;
		case 4:
			Toast.makeText(getApplicationContext(), "menu4", Toast.LENGTH_SHORT).show();
			break;
		case 5:
			Toast.makeText(getApplicationContext(), "menu5", Toast.LENGTH_SHORT).show();
			break;
		case 6:
			Toast.makeText(getApplicationContext(), "menu6", Toast.LENGTH_SHORT).show();
			break;
		case 7:
			Toast.makeText(getApplicationContext(), "menu7", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return true;
	}

}
