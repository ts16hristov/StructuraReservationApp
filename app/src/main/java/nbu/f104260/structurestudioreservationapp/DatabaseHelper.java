package nbu.f104260.structurestudioreservationapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import nbu.f104260.structurestudioreservationapp.entities.Appointment;
import nbu.f104260.structurestudioreservationapp.entities.Barber;
import nbu.f104260.structurestudioreservationapp.entities.Service;
import nbu.f104260.structurestudioreservationapp.entities.Time;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context, String DATABASE_NAME) {
        super(context, DATABASE_NAME, null, 1);
    }

    private static final String DATABASE_NAME = "Signup.db";
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAllUsersTable(db);
        createPossitionTable(db);
        createServicesTable(db);
        createWorkingHoursTable(db);
        createBarberTable(db);
        createPriceListTable(db);
        createAppointmentsTable(db);

        insertInitialData(db);
        insertInitialPossitionData(db);
        insertInitialServiceData(db);
        insertInitialWorkingHoursData(db);
        insertInitialBarberData(db);
        insertInitialPriceListData(db);
        insertInitialAppointmentData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS allUsers");
        db.execSQL("DROP TABLE IF EXISTS possition");
        db.execSQL("DROP TABLE IF EXISTS services");
        db.execSQL("DROP TABLE IF EXISTS working_hours");
        db.execSQL("DROP TABLE IF EXISTS barber");
        db.execSQL("DROP TABLE IF EXISTS priceList");
        db.execSQL("DROP TABLE IF EXISTS appointments");

        onCreate(db);

    }

    private void createAllUsersTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE allUsers (" +
                "email TEXT PRIMARY KEY," +
                "first_name TEXT," +
                "last_name TEXT," +
                "password TEXT," +
                "phoneNumber TEXT)";
        db.execSQL(createTableSQL);
    }

    private void insertInitialData(SQLiteDatabase db) {
        insertUser(db, "test1@gmail.com", "Test", "Testov", "Aa12345678?", "0896904928");
        insertUser(db, "plamen@gmail.com", "Plamen", "Plamenov", "Aa123456?", "0896901234");
    }

    private void insertUser(SQLiteDatabase db, String email, String firstName, String lastName, String password, String phoneNumber) {
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("password", password);
        values.put("phoneNumber", phoneNumber);
        db.insert("allUsers", null, values);
    }

    private void createPossitionTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE possition (" +
                "possition TEXT PRIMARY KEY)";
        db.execSQL(createTableSQL);
    }


    public void insertInitialPossitionData(SQLiteDatabase db) {
        insertPossition(db, "BARBER");
        insertPossition(db, "TOP-BARBER");
        insertPossition(db, "CHIEF-BARBER");
    }

    public void insertPossition(SQLiteDatabase db, String possition) {
        ContentValues values = new ContentValues();
        values.put("possition", possition);
        db.insert("possition", null, values);
    }

    private void createServicesTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE services (" +
                "service_type TEXT PRIMARY KEY)";
        db.execSQL(createTableSQL);
    }

    public void insertInitialServiceData(SQLiteDatabase db) {
        insertService(db, "Haircut");
        insertService(db, "Combo");
        insertService(db, "Beard styling");
        insertService(db, "Father&Son");
    }

    public void insertService(SQLiteDatabase db, String serviceType) {
        ContentValues values = new ContentValues();
        values.put("service_type", serviceType);
        db.insert("services", null, values);
    }

    private void createWorkingHoursTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE working_hours (" +
                "start_hour TEXT PRIMARY KEY)";
        db.execSQL(createTableSQL);
    }

    public void insertInitialWorkingHoursData(SQLiteDatabase db) {
        insertWorkingHour(db, "10:00:00");
        insertWorkingHour(db, "11:00:00");
        insertWorkingHour(db, "12:00:00");
        insertWorkingHour(db, "13:00:00");
        insertWorkingHour(db, "14:00:00");
        insertWorkingHour(db, "15:00:00");
        insertWorkingHour(db, "16:00:00");
        insertWorkingHour(db, "17:00:00");
        insertWorkingHour(db, "18:00:00");
    }

    public void insertWorkingHour(SQLiteDatabase db, String startHour) {
        ContentValues values = new ContentValues();
        values.put("start_hour", startHour);
        db.insert("working_hours", null, values);
    }

    private void createBarberTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE barber (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "possition TEXT," +
                "FOREIGN KEY (possition) REFERENCES possition (possition) ON DELETE NO ACTION ON UPDATE CASCADE)";
        db.execSQL(createTableSQL);
    }

    public void insertInitialBarberData(SQLiteDatabase db) {
        insertBarber(db, 1, "Nikola", "BARBER");
        insertBarber(db, 2, "Kolyo", "TOP-BARBER");
        insertBarber(db, 3, "Vasko", "CHIEF-BARBER");
    }

    public void insertBarber(SQLiteDatabase db, int id, String name, String possition) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("possition", possition);
        db.insert("barber", null, values);
    }

    private void createPriceListTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE priceList (" +
                "barberId INTEGER," +
                "service TEXT," +
                "price INTEGER," +
                "PRIMARY KEY (barberId, service)," +
                "FOREIGN KEY (service) REFERENCES services (service_type) ON DELETE CASCADE ON UPDATE CASCADE," +
                "FOREIGN KEY (barberId) REFERENCES barber (id) ON DELETE CASCADE ON UPDATE CASCADE)";
        db.execSQL(createTableSQL);
    }

    public void insertInitialPriceListData(SQLiteDatabase db) {
        insertPriceList(db, 1, "Haircut", 20);
        insertPriceList(db, 1, "Combo", 30);
        insertPriceList(db, 1, "Beard styling", 15);
        insertPriceList(db, 1, "Father&Son", 35);
        insertPriceList(db, 2, "Beard styling", 20);
        insertPriceList(db, 2, "Combo", 40);
        insertPriceList(db, 2, "Father&Son", 45);
        insertPriceList(db, 2, "Haircut", 25);
        insertPriceList(db, 3, "Beard styling", 25);
        insertPriceList(db, 3, "Combo", 50);
        insertPriceList(db, 3, "Father&Son", 55);
        insertPriceList(db, 3, "Haircut", 30);
    }

    public void insertPriceList(SQLiteDatabase db, int barberId, String service, int price) {
        ContentValues values = new ContentValues();
        values.put("barberId", barberId);
        values.put("service", service);
        values.put("price", price);
        db.insert("priceList", null, values);
    }


    private void createAppointmentsTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE appointments (" +
                "barber_id INTEGER NOT NULL," +
                "working_hour TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "client_email TEXT NOT NULL," +
                "service TEXT NOT NULL," +
                "price INTEGER," +
                "PRIMARY KEY (barber_id, working_hour, date)," +
                "FOREIGN KEY (client_email) REFERENCES allUsers (email) ON DELETE NO ACTION ON UPDATE CASCADE," +
                "FOREIGN KEY (service) REFERENCES services (service_type) ON DELETE NO ACTION ON UPDATE CASCADE," +
                "FOREIGN KEY (working_hour) REFERENCES working_hours (start_hour) ON DELETE NO ACTION ON UPDATE CASCADE," +
                "FOREIGN KEY (barber_id) REFERENCES barber (id) ON DELETE NO ACTION ON UPDATE CASCADE)";
        db.execSQL(createTableSQL);
    }

    public void insertInitialAppointmentData(SQLiteDatabase db) {
        insertAppointment(db, 1, "11:00:00", "2024-06-20", "test1@gmail.com", "Haircut", 20);
        insertAppointment(db, 2, "10:00:00", "2024-06-10", "test1@gmail.com", "Combo", 40);
        insertAppointment(db, 2, "15:00:00", "2024-06-01", "test1@gmail.com", "Haircut", 25);
        insertAppointment(db, 2, "13:00:00", "2024-07-01", "plamen@gmail.com", "Father&Son", 45);
        insertAppointment(db, 2, "11:00:00", "2024-07-09", "plamen@gmail.com", "Combo", 40);
        insertAppointment(db, 2, "11:00:00", "2024-07-02", "plamen@gmail.com", "Combo", 40);
        insertAppointment(db, 2, "12:00:00", "2024-07-02", "plamen@gmail.com", "Combo", 40);
        insertAppointment(db, 2, "17:00:00", "2024-07-01", "plamen@gmail.com", "Combo", 40);
        insertAppointment(db, 2, "11:00:00", "2024-07-06", "test1@gmail.com", "Haircut", 25);
    }

    public void insertAppointment(SQLiteDatabase db, int barberId, String workingHour, String date, String clientEmail, String service, int price) {
        ContentValues values = new ContentValues();
        values.put("barber_id", barberId);
        values.put("working_hour", workingHour);
        values.put("date", date);
        values.put("client_email", clientEmail);
        values.put("service", service);
        values.put("price", price);
        db.insert("appointments", null, values);
    }


    public Boolean insertData(String email,String firstName, String lastName, String password, String phone_number){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email",email);
        contentValues.put("first_name",firstName);
        contentValues.put("last_name",lastName);
        contentValues.put("password",password);
        contentValues.put("phoneNumber",phone_number);
        long result = MyDatabase.insert("allUsers", null, contentValues);

        return result != -1;

    }

    public Boolean checkEmail(String email){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = MyDatabase.rawQuery("select * from allUsers where email = ?", new String[]{email});

        return cursor.getCount() > 0;
    }

    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = MyDatabase.rawQuery("select * from allUsers where email = ? and password = ?", new String[]{email, password});

        return cursor.getCount() > 0;
    }

    public Boolean deleteUser(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = "email = ?";
        String[] selectionArgs = { email };
        int deletedRows = db.delete("allUsers", selection, selectionArgs);
        db.close();
        return deletedRows > 0;
    }

    public Boolean updateUser(String email, String newEmail, String firstName, String lastName, String password, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Add values only if they are not null

        if (newEmail != null) {
            contentValues.put("email", newEmail);
        }
        if (firstName != null) {
            contentValues.put("first_name", firstName);
        }
        if (lastName != null) {
            contentValues.put("last_name", lastName);
        }
        if (password != null) {
            contentValues.put("password", password);
        }
        if (phoneNumber != null) {
            contentValues.put("phoneNumber", phoneNumber);
        }

        int result = db.update("allUsers", contentValues, "email = ?", new String[]{email});
        db.close();
        return result > 0;
    }

    public Cursor getUserData(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM allUsers WHERE email = ?", new String[]{email});
    }

    public boolean checkPassword(String email, String oldPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM allUsers WHERE email = ?", new String[]{email});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String storedPassword = cursor.getString(cursor.getColumnIndex("password"));
                cursor.close();
                return storedPassword.equals(oldPassword);
            }
            cursor.close();
        }
        return false;
    }

    public ArrayList<Barber>  getAllBarbers(){
        ArrayList<Barber> barbers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery ="SELECT * FROM barber";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){

            int idColumn = cursor.getColumnIndex("id");
            int nameColumn = cursor.getColumnIndex("name");
            int positionColumn = cursor.getColumnIndex("possition");

            do {
                Barber barber = new Barber();
                barber.setId(cursor.getInt(idColumn));
                barber.setName(cursor.getString(nameColumn));
                barber.setPosition(cursor.getString(positionColumn));
                barbers.add(barber);
            } while(cursor.moveToNext());
        } else {
            System.out.println("AAAAAAAAAAAAAAAAS ----- CANNOT READ!");
        }

        cursor.close();
        db.close();

        return barbers;

    }

    public ArrayList<Service> getAllServicesById(Long barberId) {
        ArrayList<Service> services = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT service, price FROM priceList WHERE barberId = ?", new String[]{String.valueOf(barberId)});

        if(cursor.moveToNext()){
            int serviceColumn = cursor.getColumnIndex("service");
            int priceColumn = cursor.getColumnIndex("price");

            do {
                Service service = new Service();
                service.setName(cursor.getString(serviceColumn));
                service.setPrice(cursor.getInt(priceColumn));
                services.add(service);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return services;
    }

    public ArrayList<Time> getAvailableWorkingHours(long barberId, String date) {
        ArrayList<Time> times= new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT wh.start_hour " +
                "FROM working_hours wh " +
                "WHERE NOT EXISTS (" +
                "    SELECT 1 " +
                "    FROM appointments a " +
                "    WHERE a.barber_id = ? " +
                "      AND a.date = ? " +
                "      AND a.working_hour = wh.start_hour" +
                ")";

        String[] selectionArgs = {String.valueOf(barberId), date};
        Cursor cursor =  db.rawQuery(query, selectionArgs);
        if(cursor.moveToNext()){
            int timeColumn = cursor.getColumnIndex("start_hour");

            do {
                Time time = new Time();
                time.setTime(cursor.getString(timeColumn));
                times.add(time);
                } while(cursor.moveToNext());
            }

            cursor.close();
            db.close();

            return times;
    }

    public boolean insertAppointment(Appointment appointment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("barber_id", appointment.getBarberId());
        values.put("working_hour", appointment.getTime());
        values.put("date", appointment.getDate());
        values.put("client_email", appointment.getClientEmail());
        values.put("service", appointment.getService());
        values.put("price", appointment.getPrice());

        long newRowId = db.insert("appointments", null, values);
        db.close();
        if(newRowId > 0){
            return true;
        } else {
            return false;
        }
    }

    public String getBarberNameById(long barberId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"name"};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(barberId)};

        Cursor cursor = db.query("barber", projection, selection, selectionArgs,
                null, null, null);

        String barberName = null;
        if (cursor != null && cursor.moveToFirst()) {
            barberName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
        }

        return barberName;
    }

    public ArrayList<Appointment> getAppointmentsByEmail(String email) {
        ArrayList<Appointment> appointments = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM appointments WHERE client_email = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndex("barber_id");
            int dateI = cursor.getColumnIndex("date");
            int wh = cursor.getColumnIndex("working_hour");
            int ser = cursor.getColumnIndex("service");
            int pri = cursor.getColumnIndex("price");
            do {
                long barberId = cursor.getLong(id);
                String workingHour = cursor.getString(wh);
                String date = cursor.getString(dateI);
                String service = cursor.getString(ser);
                int price = cursor.getInt(pri);

                Appointment appointment = new Appointment(barberId, service, date, workingHour,email, price);
                appointments.add(appointment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return appointments;
    }
}
