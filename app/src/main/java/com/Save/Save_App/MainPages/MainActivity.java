package com.Save.Save_App.MainPages;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.Save.Save_App.Helpers.BackgroundService;
import com.Save.Save_App.Helpers.HelperFunctions;
import com.Save.Save_App.Helpers.SharedPrefsUtil;
import com.Save.Save_App.Interfaces.Bill;
import com.Save.Save_App.Interfaces.User;
import com.Save.Save_App.MainPages.Budget.BudgetPage;
import com.Save.Save_App.R;
import com.Save.Save_App.RergistrationAndLogin.Login;
import com.Save.Save_App.Settings.NotificationPage;
import com.Save.Save_App.Settings.settings;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    SharedPrefsUtil sharedPrefsUtil;
    HelperFunctions helperFunc;
    User user;
    AnyChartView pieChart;
    Pie pie;
    TextView message;

    Timer timer = new Timer();
    final Handler handler = new Handler();
    TimerTask timertask = new TimerTask() {
        @Override
        public void run() {
            handler.post(new Runnable() {
                public void run() {
                    startService(new Intent(getApplicationContext(), BackgroundService.class));
                }
            });
        }
    };

    

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helperFunc = new HelperFunctions();
        sharedPrefsUtil = new SharedPrefsUtil(this);
        String email = sharedPrefsUtil.get("email_income", "");
        user = sharedPrefsUtil.get(email, User.class, new User());

        //chart
        pieChart = findViewById(R.id.any_chart_view);
        pie = AnyChart.pie();
        pieChart.setChart(pie);
        final ArrayList<DataEntry> dataEntries = new ArrayList<>();


        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        String DateATime = formatter.format(date);

        String CurrentMonth = user.getCurrentMonth();

        if (CurrentMonth == null)
            user.setCurrentMonth(DateATime);

        if (CurrentMonth.equals("01")){
            user.setMonthBills(user.getBillsAmount(), 0);
        }
        else if (CurrentMonth.equals("02")){
            user.setMonthBills(user.getBillsAmount(), 1);
        }
        else if (CurrentMonth.equals("03")){
            user.setMonthBills(user.getBillsAmount(), 2);
        }
        else if (CurrentMonth.equals("04")){
            user.setMonthBills(user.getBillsAmount(), 3);
        }
        else if (CurrentMonth.equals("05")){
            user.setMonthBills(user.getBillsAmount(), 4);
        }
        else if (CurrentMonth.equals("06")){
            user.setMonthBills(user.getBillsAmount(), 5);
        }
        else if (CurrentMonth.equals("07")){
            user.setMonthBills(user.getBillsAmount(), 6);
        }
        else if (CurrentMonth.equals("08")){
            user.setMonthBills(user.getBillsAmount(), 7);
        }
        else if (CurrentMonth.equals("09")){
            user.setMonthBills(user.getBillsAmount(), 8);
        }
        else if (CurrentMonth.equals("10")){
            user.setMonthBills(user.getBillsAmount(), 9);
        }
        else if (CurrentMonth.equals("11")){
            user.setMonthBills(user.getBillsAmount(), 10);
        }
        else {
            user.setMonthBills(user.getBillsAmount(), 11);
        }

        if (!CurrentMonth.equals(DateATime)){
            user.setIncome(user.getOriginalIncome());
            user.setBillsAmount(0);
            user.setCurrentMonth(DateATime);
        }
        sharedPrefsUtil.put(email, User.class, user);
        ////


        ArrayList<Bill> bills = user.getBills();
        if(bills != null)
        {
            float[] billCategoryMap = helperFunc.mapBillCategories(bills);

            String []categoryString = {"Rent", "Services", "Food", "Entertainment", "Clothes", "Other"};
            float billTotal = 0;
            for(int i = 0; i < billCategoryMap.length; ++i ){
                billTotal += billCategoryMap[i];
                dataEntries.add(new ValueDataEntry(categoryString[i], billCategoryMap[i]));
            }
            dataEntries.add(new ValueDataEntry("Savings", user.getIncome() - user.getBillsAmount()));
        }

        pie.data(dataEntries);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pie.data(dataEntries);

            }
        }, 100);

        message = findViewById(R.id.message);
        if(user.getBillsAmount() > user.getIncome()){
            message.setText("You have pass your limit!!");
            message.setBackgroundResource(R.drawable.red_box);
        }
        else{
            message.setText("You still within your limit!!");
            message.setBackgroundResource(R.drawable.green_box);
        }

        Toolbar topbar = findViewById(R.id.topbar);
        topbar.setTitle("Home");
        topbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(topbar);
        topbar.setNavigationIcon(R.drawable.ic_notifications_black_24dp);
        topbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NotificationPage.class);
                startActivity(i);
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigationView);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        break;
                    case R.id.account:
                        Intent a = new Intent(MainActivity.this, AccountPage.class);
                        startActivity(a);
                        break;
                    case R.id.budget:
                        Intent b = new Intent(MainActivity.this, BudgetPage.class);
                        startActivity(b);
                        break;
                    case R.id.history:
                        Intent c = new Intent(MainActivity.this, History.class);
                        startActivity(c);
                        break;
                    case R.id.paybills:
                        Intent d = new Intent(MainActivity.this, Paybills.class);
                        startActivity(d);
                        break;
                }
                return false;
            }
        });
        timer.schedule(timertask, 0, 86400000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent i = new Intent(this, settings.class);
                startActivity(i);
                break;
            case R.id.item2:
                Intent b = new Intent(this, Login.class);
                startActivity(b);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

}
