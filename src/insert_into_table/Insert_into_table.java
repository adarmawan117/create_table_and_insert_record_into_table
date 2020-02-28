package insert_into_table;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Insert_into_table {
    
    String replace_underscore(String nama) {
        int panjang = nama.length();
        char hasil[] = new char[panjang];
        
        //System.out.println(nama);
        for(int i = 0; i < panjang; i++) {
            if(nama.charAt(i) == ' ') {
                hasil[i] = '_';
            } else {
                hasil[i] = nama.charAt(i);
            }
        }
        //System.out.println(nama);
        // convert dari array of char menjadi string
        return new String(hasil);
    }
    
    ArrayList <String> nama_gudang = new ArrayList <String> (
            Arrays.asList(  "Alkes",
                            "Basah",
                            "Berkala",
                            "Dukkes",
                            "Faskes",
                            "Harpasat",
                            "Kat",
                            "Kering",
                            "Lakalatpur Daerah",
                            "Lakalatpur Pusat",
                            "Pdf",
                            "Seldik",
                            "Werfing")
    );
    void create_table() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db_gudang_030201";
        String username = "root";
        String password = "";

        Connection conn = null;
        Statement stmt = null;
        
        try {
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            stmt = conn.createStatement();
            
            // membuat semua tabel, sesuai yang ada di ArrayList "nama_gudang"
            for(String nama : nama_gudang) {
                // karena penamaan tabel tidak boleh menggunakan spasi, 
                //maka perlu untuk merubah spasi menjadi underscore terlebih dahulu
                nama = replace_underscore(nama);
                
                final String CREATE_TABLE_SQL = "CREATE TABLE "+ nama
                                                + "(nama_obat VARCHAR(50) NOT NULL)";
                stmt.executeUpdate(CREATE_TABLE_SQL);
                System.out.println("Table created");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close connection
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    ArrayList <String> list_file_text = new ArrayList <String> (
                Arrays.asList(  "List Obat Gudang Alkes.txt",
                                "List Obat Gudang Basah.txt",
                                "List Obat Gudang Berkala.txt",
                                "List Obat Gudang Dukkes.txt",
                                "List Obat Gudang Faskes.txt",
                                "List Obat Gudang Harpasat.txt",
                                "List Obat Gudang Kat.txt",
                                "List Obat Gudang Kering.txt",
                                "List Obat Gudang Lakalatpur Daerah.txt",
                                "List Obat Gudang Lakalatpur Pusat.txt",
                                "List Obat Gudang Pdf.txt",
                                "List Obat Gudang Seldik.txt",
                                "List Obat Gudang Werfing.txt"
                )
        );
    void insert() {
        try{
            // create a mysql database connection
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/db_gudang_030201";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "root", "");
            
            // mengulang semua list file
            for(String nama_file : list_file_text) {
                
                int idx = list_file_text.indexOf(nama_file);
                
                File file = new File(nama_file); 
                BufferedReader br; 
                try {
                    br = new BufferedReader(new FileReader(file));
                    String nama_obat;
                    
                    // mengulang semua isi (text) file
                    while ((nama_obat = br.readLine()) != null) { 
                        //System.out.println(nama_obat);
                        
                        // nama_gudang.get(idx) merupakan nama tabel yang akan ditambahkan
                        String query =  " insert into "+ nama_gudang.get(idx) 
                                        +" (nama_obat)"
                                        /*
                                Tanda tanya disini akan berpengaruh terhadap banyaknya syntax
                                seperti pada baris ke 144.
                                misal:
                                values (?, ?, ?)
                                maka, preparedStmt.setString() akan ada 3 buah,
                                dan angkanya akan bertambah, jadi 1, lalu 2, lalu 3
                                        */
                                        + " values (?)";
                        // create the mysql insert preparedstatement
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setString (1, nama_obat);
                        // execute the preparedstatement
                        preparedStmt.execute();
                    } 
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Insert_into_table.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Insert_into_table.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // menutup koneksi database
            conn.close();
          } catch (Exception e){
            e.printStackTrace();
          } 
    }
    
    public static void main(String[] args) {
        Insert_into_table iit = new Insert_into_table();
        iit.create_table();
        iit.insert();
    } // end of main   
}