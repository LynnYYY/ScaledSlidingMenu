package com.example.se7en.slidingmenu;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

public class MainActivity extends Activity {
    private SlidingMenu mSlidingMenu;
    private FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getFragmentManager();

        mSlidingMenu = (SlidingMenu)findViewById(R.id.id_sliding_menu);
        mSlidingMenu.setMenu(mFragmentManager.beginTransaction(),new MenuFragment());
        mSlidingMenu.setContent(mFragmentManager.beginTransaction(), new ContentFragment());
    }

    public void toogleMenu(){
        mSlidingMenu.toogleMenu();
    }
}
