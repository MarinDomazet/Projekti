package com.example.sqltest;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.NestedScrollView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    NestedScrollView nestedScrollView;
    LinearLayout containerLayout;
    int layoutCount = 0;
    int MAX_LAYOUTS = 8;
    TextView display1_name, /*display1_Nation, display1_League,
            display1_Club,*/ display1_Position, display1_Age, display1_ShirtNumber,
            tvPozicijaResult;

            /*tvNameResult, tvNationResult, tvLeagueResult, tvClubResult,
            tvPositionResult, tvAgeResult, tvShirtResult*/
    Button btnEnter;
    ImageView ivSlika, ivSlikaDrzava, ivSlikaKlub, ivNationDisplay, ivLeagueDisplay, ivClubDisplay;
    LinearLayout LinLay1;
    AutoCompleteTextView autoCompleteTextView;
    List<String> namesList = new ArrayList<>();
    int i=1, randIgracID, guessNumber=1;
    String randIDIgrac, randName, randNation, randLeague,randClub, randPosition, randAge, randShirtNumber;
    boolean result;
    Bitmap bitmap, bitmap2, bitmap3, bitmap4, bitmap2Res, bitmap3Res, bitmap4Res;
    byte[] imageBytes, imageBytes2, imageBytes3, imageBytes4;
    private static String ip = "10.0.5.248";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "Igraci";
    private static String username = "sa";
    private static String password = "petnaestidvadeset";
    private static String url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database;

    private Connection connection = null;
    boolean leagueShown=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        nestedScrollView = findViewById(R.id.nestedScrollView);
        containerLayout = findViewById(R.id.containerLayout);

        /*
        tvNameResult = (TextView) findViewById(R.id.tvNameResult);
        tvNationResult = (TextView) findViewById(R.id.tvNationResult);
        tvLeagueResult = (TextView) findViewById(R.id.tvLeagueResult);
        tvClubResult = (TextView) findViewById(R.id.tvClubResult);
        tvPositionResult = (TextView) findViewById(R.id.tvPositionResult);
        tvAgeResult = (TextView) findViewById(R.id.tvAgeResult);
        tvShirtResult = (TextView) findViewById(R.id.tvShirtResult);
         */

        tvPozicijaResult = (TextView) findViewById(R.id.tvPozicijaResult);

        btnEnter = (Button) findViewById(R.id.btnEnter);

        ivSlika = (ImageView) findViewById(R.id.ivSlika);
        ivSlikaDrzava = (ImageView) findViewById(R.id.ivSlikaDrzava);
        ivSlikaKlub = (ImageView) findViewById(R.id.ivSlikaKlub);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        DialogShow  d = new DialogShow(this);

        //LinLay1.setVisibility(View.GONE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username, password);
            Toast.makeText(MainActivity.this, "Connection DB Success", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Connection DB Error", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Connection DB Failure", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, namesList);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(adapter);

        if (connection != null) {
            Statement stat = null;
            try {
                stat = connection.createStatement();
                ResultSet rs = stat.executeQuery("Select ImePrezime from IgraciTablica");
                while (rs.next()) {
                    String name = rs.getString("ImePrezime");
                    namesList.add(name);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Connection null", Toast.LENGTH_SHORT).show();
        }

        Random rand = new Random();
        //randIgracID = 42;
        randIgracID = rand.nextInt(309 - 1) + 1;
        Toast.makeText(this,""+randIgracID, Toast.LENGTH_SHORT).show();
        String queryRandID = "SELECT IDIgrac, ImePrezime, Drzavljanstvo, Liga, Klub, Pozicija, Godine, BrojDres, Slika, LigaSlika, KlubSlika, DrzavaSlika FROM IgraciTablica WHERE IDIgrac = '" + randIgracID + "'";
        if (connection != null) {
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryRandID);

                if (resultSet.next()) {
                    if(resultSet.getBytes("Slika")==null || resultSet.getBytes("LigaSlika")==null || resultSet.getBytes("KlubSlika")==null || resultSet.getBytes("DrzavaSlika")==null){
                        Toast.makeText(this, "Nula", Toast.LENGTH_SHORT).show();
                    }else{
                        imageBytes = resultSet.getBytes("Slika");
                        bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                        imageBytes2 = resultSet.getBytes("LigaSlika");
                        bitmap2Res = BitmapFactory.decodeByteArray(imageBytes2, 0, imageBytes2.length);

                        imageBytes3 = resultSet.getBytes("KlubSlika");
                        bitmap3Res = BitmapFactory.decodeByteArray(imageBytes3, 0, imageBytes3.length);

                        imageBytes4 = resultSet.getBytes("DrzavaSlika");
                        bitmap4Res = BitmapFactory.decodeByteArray(imageBytes4, 0, imageBytes4.length);
                    }

                    randIDIgrac = resultSet.getString("IDIgrac");
                    randName = resultSet.getString("ImePrezime");
                    randNation = resultSet.getString("Drzavljanstvo");
                    randLeague = resultSet.getString("Liga");
                    randClub = resultSet.getString("Klub");
                    randPosition = resultSet.getString("Pozicija");
                    randAge = resultSet.getString("Godine");
                    randShirtNumber = resultSet.getString("BrojDres");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Connection null", Toast.LENGTH_SHORT).show();
        }

        //autoCompleteTextView.setHintTextColor(Color.BLACK);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    String player = autoCompleteTextView.getText().toString();
                    String query = "SELECT IDIgrac, ImePrezime, Drzavljanstvo, Liga, Klub, Pozicija, Godine, BrojDres, Slika, LigaSlika, KlubSlika, DrzavaSlika FROM IgraciTablica WHERE ImePrezime = '" + player + "'";

                    if (player.isEmpty()) {
                        Toast.makeText(MainActivity.this, R.string.input, Toast.LENGTH_SHORT).show();
                    } else {

                        if (layoutCount < MAX_LAYOUTS) {
                            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                            View stackableLayout = inflater.inflate(R.layout.stackable_layout, containerLayout, false);

                            LinLay1 = stackableLayout.findViewById(R.id.LinLay1);
                            display1_name = stackableLayout.findViewById(R.id.display1_name);
                            /*
                            display1_Nation = stackableLayout.findViewById(R.id.display1_nation);
                            display1_League = stackableLayout.findViewById(R.id.display1_ligue);
                            display1_Club = stackableLayout.findViewById(R.id.display1_club);
                            */
                            display1_Position = stackableLayout.findViewById(R.id.display1_position);
                            display1_Age = stackableLayout.findViewById(R.id.display1_age);
                            display1_ShirtNumber = stackableLayout.findViewById(R.id.display1_shirtNumber);

                            ivNationDisplay = stackableLayout.findViewById(R.id.ivNationDisplay);
                            ivLeagueDisplay = stackableLayout.findViewById(R.id.ivLeagueDisplay);
                            ivClubDisplay = stackableLayout.findViewById(R.id.ivClubDisplay);

                            //display1_Nation.setBackgroundColor(Color.WHITE);
                            ivNationDisplay.setBackgroundColor(Color.WHITE);

                            if (connection != null) {
                                Statement statement = null;
                                try {
                                    statement = connection.createStatement();

                                    display1_name.setTextColor(Color.WHITE);
                                    /*display1_Nation.setTextColor(Color.WHITE);
                                    display1_League.setTextColor(Color.WHITE);
                                    display1_Club.setTextColor(Color.WHITE);
                                     */
                                    display1_Position.setTextColor(Color.WHITE);
                                    display1_Age.setTextColor(Color.WHITE);
                                    display1_ShirtNumber.setTextColor(Color.WHITE);

                                    ResultSet resultSet = statement.executeQuery(query);
                                    if (!resultSet.next()) {
                                        Toast.makeText(MainActivity.this, R.string.errorDB, Toast.LENGTH_SHORT).show();
                                    } else {
                                        if(resultSet.getBytes("Slika")==null || resultSet.getBytes("LigaSlika")==null || resultSet.getBytes("KlubSlika")==null || resultSet.getBytes("DrzavaSlika")==null){
                                            Toast.makeText(MainActivity.this, "Null", Toast.LENGTH_SHORT).show();
                                        }else{
                                            imageBytes2 = resultSet.getBytes("LigaSlika");
                                            bitmap2 = BitmapFactory.decodeByteArray(imageBytes2, 0, imageBytes2.length);

                                            imageBytes3 = resultSet.getBytes("KlubSlika");
                                            bitmap3 = BitmapFactory.decodeByteArray(imageBytes3, 0, imageBytes3.length);

                                            imageBytes4 = resultSet.getBytes("DrzavaSlika");
                                            bitmap4 = BitmapFactory.decodeByteArray(imageBytes4, 0, imageBytes4.length);
                                        }
                                            guessNumber++;
                                        autoCompleteTextView.setHint(getString(R.string.guess) + " " + guessNumber + " " + getString(R.string.of));
                                        if(guessNumber>8){
                                            autoCompleteTextView.setHint("");
                                        }
                                            display1_name.setText(resultSet.getString("ImePrezime"));
                                            if ((resultSet.getString("ImePrezime")).equals(randName)) {
                                                display1_name.setBackgroundColor(Color.GREEN);
                                                /*tvNameResult.setBackgroundColor(Color.GREEN);
                                                tvNameResult.setText(resultSet.getString("ImePrezime"));
                                                tvNameResult.setTextColor(Color.BLACK);
                                                 */
                                            } else {
                                                display1_name.setBackgroundColor(Color.GRAY);
                                            }
                                            //
                                            ivNationDisplay.setImageBitmap(bitmap4);
                                            //display1_Nation.setText(resultSet.getString("Drzavljanstvo"));
                                            if ((resultSet.getString("Drzavljanstvo")).equals(randNation)) {
                                                //display1_Nation.setBackgroundColor(Color.GREEN);
                                                ivSlikaDrzava.setImageBitmap(bitmap4);
                                                ivNationDisplay.setBackgroundColor(Color.GREEN);
                                                /*tvNationResult.setBackgroundColor(Color.GREEN);
                                                tvNationResult.setText(resultSet.getString("Drzavljanstvo"));
                                                tvNationResult.setTextColor(Color.BLACK);
                                                */
                                            } else {
                                                ivNationDisplay.setBackgroundColor(Color.GRAY);
                                                //display1_Nation.setBackgroundColor(Color.GRAY);
                                            }
                                            //
                                            ivLeagueDisplay.setImageBitmap(bitmap2);
                                            //display1_League.setText(resultSet.getString("Liga"));
                                            if ((resultSet.getString("Liga")).equals(randLeague)) {
                                                ivLeagueDisplay.setBackgroundColor(Color.GREEN);
                                                //display1_League.setBackgroundColor(Color.GREEN);
                                                //if(leagueShown==false){
                                                    //ivSlikaKlub.setImageBitmap(bitmap2);
                                                   // leagueShown=true;
                                                //}
                                                /*tvLeagueResult.setBackgroundColor(Color.GREEN);
                                                tvLeagueResult.setText(resultSet.getString("Liga"));
                                                tvLeagueResult.setTextColor(Color.BLACK);*/
                                            } else {
                                                ivLeagueDisplay.setBackgroundColor(Color.GRAY);
                                                //display1_League.setBackgroundColor(Color.GRAY);
                                            }
                                            //
                                            ivClubDisplay.setImageBitmap(bitmap3);
                                            //display1_Club.setText(resultSet.getString("Klub"));
                                            if ((resultSet.getString("Klub")).equals(randClub)) {
                                                ivClubDisplay.setBackgroundColor(Color.GREEN);
                                                //display1_Club.setBackgroundColor(Color.GREEN);
                                                ivSlikaKlub.setImageBitmap(bitmap3);
                                                /*tvClubResult.setBackgroundColor(Color.GREEN);
                                                tvClubResult.setText(resultSet.getString("Klub"));
                                                tvClubResult.setTextColor(Color.BLACK);
                                                 */
                                            } else {
                                                ivClubDisplay.setBackgroundColor(Color.GRAY);
                                                //display1_Club.setBackgroundColor(Color.GRAY);
                                            }
                                            //
                                            display1_Position.setText(resultSet.getString("Pozicija"));
                                            if ((resultSet.getString("Pozicija")).equals(randPosition)) {
                                                display1_Position.setTextColor(Color.BLACK);
                                                display1_Position.setBackgroundColor(Color.GREEN);
                                                tvPozicijaResult.setText(""+resultSet.getString("Pozicija"));
                                                /*tvPositionResult.setBackgroundColor(Color.GREEN);
                                                tvPositionResult.setText(resultSet.getString("Pozicija"));
                                                tvPositionResult.setTextColor(Color.BLACK);*/
                                            } else {
                                                display1_Position.setBackgroundColor(Color.GRAY);
                                            }
                                            //
                                            if (Integer.parseInt(resultSet.getString("Godine")) > Integer.parseInt(randAge)) {
                                                display1_Age.setText("▼" + resultSet.getString("Godine"));
                                                if ((resultSet.getString("Godine")).equals(randAge)) {
                                                    display1_Age.setBackgroundColor(Color.GREEN);
                                                    /*tvAgeResult.setBackgroundColor(Color.GREEN);
                                                    tvAgeResult.setText("▼" + resultSet.getString("Godine"));
                                                    tvAgeResult.setTextColor(Color.BLACK);*/
                                                } else {
                                                    display1_Age.setBackgroundColor(Color.GRAY);
                                                }
                                            } else if (Integer.parseInt(resultSet.getString("Godine")) < Integer.parseInt(randAge)) {
                                                display1_Age.setText("▲" + resultSet.getString("Godine"));
                                                if ((resultSet.getString("Godine")).equals(randAge)) {
                                                    display1_Age.setBackgroundColor(Color.GREEN);
                                                    /*tvAgeResult.setBackgroundColor(Color.GREEN);
                                                    tvAgeResult.setText("▲" + resultSet.getString("Godine"));
                                                    tvAgeResult.setTextColor(Color.BLACK);*/
                                                } else {
                                                    display1_Age.setBackgroundColor(Color.GRAY);
                                                }
                                            } else {
                                                display1_Age.setText(resultSet.getString("Godine"));
                                                if ((resultSet.getString("Godine")).equals(randAge)) {
                                                    display1_Age.setTextColor(Color.BLACK);
                                                    display1_Age.setBackgroundColor(Color.GREEN);
                                                    /*tvAgeResult.setBackgroundColor(Color.GREEN);
                                                    tvAgeResult.setText(resultSet.getString("Godine"));
                                                    tvAgeResult.setTextColor(Color.BLACK);*/
                                                } else {
                                                    display1_Age.setBackgroundColor(Color.GRAY);
                                                }
                                            }
                                            //
                                            //display1_ShirtNumber.setText("#" + resultSet.getString("BrojDres"));
                                            if (Integer.parseInt(resultSet.getString("BrojDres")) > Integer.parseInt(randShirtNumber)) {
                                                display1_ShirtNumber.setText("#" + resultSet.getString("BrojDres")+"▼");
                                                if ((resultSet.getString("BrojDres")).equals(randShirtNumber)) {
                                                    display1_ShirtNumber.setBackgroundColor(Color.GREEN);
                                                } else {
                                                    display1_ShirtNumber.setBackgroundColor(Color.GRAY);
                                                }
                                            } else if (Integer.parseInt(resultSet.getString("BrojDres")) < Integer.parseInt(randShirtNumber)) {
                                                display1_ShirtNumber.setText("#" + resultSet.getString("BrojDres")+"▲");
                                                if ((resultSet.getString("BrojDres")).equals(randShirtNumber)) {
                                                    display1_ShirtNumber.setBackgroundColor(Color.GREEN);
                                                } else {
                                                    display1_ShirtNumber.setBackgroundColor(Color.GRAY);
                                                }
                                            }
                                            else{
                                                display1_ShirtNumber.setText(resultSet.getString("BrojDres"));
                                                if ((resultSet.getString("BrojDres")).equals(randShirtNumber)) {
                                                    display1_ShirtNumber.setTextColor(Color.BLACK);
                                                    display1_ShirtNumber.setBackgroundColor(Color.GREEN);
                                                    /*tvShirtResult.setBackgroundColor(Color.GREEN);
                                                    tvShirtResult.setText(resultSet.getString("BrojDres"));
                                                    tvShirtResult.setTextColor(Color.BLACK);*/
                                                } else {
                                                    display1_ShirtNumber.setBackgroundColor(Color.GRAY);
                                                }
                                            }
                                            //

                                            LinLay1.setVisibility(View.VISIBLE);

                                            if (Integer.parseInt(randIDIgrac) == Integer.parseInt(resultSet.getString("IDIgrac"))) {
                                                result = true;
                                                endGame(result);
                                            }
                                            layoutCount++;
                                            autoCompleteTextView.setText("");
                                            containerLayout.addView(stackableLayout, 0);
                                            nestedScrollView.post(() -> nestedScrollView.scrollTo(0, 0));

                                        if (guessNumber == 9) {

                                            result = false;
                                            if (bitmap != null) {
                                                ivSlika.setImageResource(R.drawable.question);
                                            } else {
                                                ivSlika.setImageResource(R.drawable.nula);
                                            }
                                            endGame(result);
                                        }
                                        
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                display1_name.setText("Connection null");
                            }
                        } else {
                    //Toast.makeText(MainActivity.this, "Maximum limit reached (8 layouts)", Toast.LENGTH_SHORT).show();
                }
                    }
            }
        });
    }
    public void endGame(boolean result){
        Intent intent = new Intent(MainActivity.this, EndGame.class);

        intent.putExtra("keyName", randName);
        intent.putExtra("keyNation", randNation);
        intent.putExtra("keyClub", randClub);
        intent.putExtra("keyPosition", randPosition);
        intent.putExtra("keyAge", randAge);
        intent.putExtra("keyShirt", randShirtNumber);
        intent.putExtra("keyResult", result);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
        byte[] compressedImageBytes = outputStream.toByteArray();
        intent.putExtra("compressedImage", compressedImageBytes);

        ByteArrayOutputStream outputStream3 = new ByteArrayOutputStream();
        bitmap3Res.compress(Bitmap.CompressFormat.PNG, 80, outputStream3);
        byte[] compressedImageBytes3 = outputStream3.toByteArray();
        intent.putExtra("compressedImage3", compressedImageBytes3);

        ByteArrayOutputStream outputStream4 = new ByteArrayOutputStream();
        bitmap4Res.compress(Bitmap.CompressFormat.PNG, 80, outputStream4);
        byte[] compressedImageBytes4 = outputStream4.toByteArray();
        intent.putExtra("compressedImage4", compressedImageBytes4);

        startActivity(intent);
    }
}
