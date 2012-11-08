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
 * @ClassName   类名：MenuActivity 
 * @Description 功能说明：
 * TODO
 ************************************************************************
 * @date        创建日期：2012-11-8
 * @author      创建人：wwx
 * @version     版本号：V1.0
 *<p>
 ***************************修订记录*************************************
 * 
 *   2012-11-8   wwx   创建该类功能。
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
//		menu.addSubMenu(men, men,men, "菜单1");
//		menu.addSubMenu(men+1, men+1,men+1, "菜单2");
//		menu.addSubMenu(men+2, men+2,men+2, "菜单3");
//		menu.addSubMenu(men+3, men+3,men+3, "菜单4");
//		menu.addSubMenu(men+4, men+4,men+4, "菜单5");
//		menu.addSubMenu(men+5, men+5,men+5, "菜单6");
//		menu.addSubMenu(men+6, men+6,men+6, "菜单7");
//		menu.addSubMenu(men+7, men+7,men+7, "菜单8");
//		menu.addSubMenu(men+8, men+8,men+8, "菜单9");
//		menu.addSubMenu(men+9, men+9,men+9, "菜单10");
		
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
