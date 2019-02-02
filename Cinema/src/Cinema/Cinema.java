package Cinema;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import java.awt.SystemColor;

@SuppressWarnings("serial")
public class Cinema extends JFrame {
	
	Connection conn = null;
	PreparedStatement state = null;
	ResultSet rs = null;
	
	
	JTable tableGenres = new JTable();
	JTable tableMovies = new JTable();
	JTable tableProjections = new JTable();
	JTable tableSearch1 = new JTable();
	JTable tableSearch2 = new JTable();
	
	
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	
	//Genres
	private JTextField textField_Genres;
	//Movies
	private JTextField textField_Title;
	private JComboBox<String> comboBox_Genre ;
	private JTextField textField_Date;
	private JTextField textField_Languege;
	private JTextField textField_Subtitles;
	private JTextField textField_Rating;
	//Projection
	private JComboBox<String> comboBox_Movie;
	private JTextField textField_Hour;
	private JTextField textField_Hall;
	//Search
		//Genadi
	private JComboBox<String> comboBoxGenres ;
	private JTextField textField_SearchLanguege;
		//Preslav
	private JTextField textField_SearchRating;
	private JTextField textField_SearchHall;
	

	public Cinema() {
		setResizable(false);
		setTitle("Cinema");
		setVisible(true);
		setSize(800,750);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel contentPane = new JPanel();
		contentPane.setBackground(new Color(128, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		
// TAB PANEL 
		tabbedPane.setBounds(12, 0, 770, 702);
		contentPane.add(tabbedPane);
		
// PANEL 1 --------------------------------------------------------------
		JPanel panelGenres = new JPanel();
		panelGenres.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Genres", null, panelGenres, null);
		tabbedPane.setBackgroundAt(0, new Color(255, 165, 0));
		panelGenres.setLayout(null);
		
		JLabel lblMolq = new JLabel("Enter genre !");
		lblMolq.setFont(new Font("Charlemagne Std", Font.BOLD | Font.ITALIC, 20));
		lblMolq.setBounds(276, 49, 201, 21);
		panelGenres.add(lblMolq);
		
		textField_Genres = new JTextField();
		textField_Genres.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField_Genres.setBounds(222, 142, 279, 37);
		panelGenres.add(textField_Genres);
		textField_Genres.setColumns(10);
		
		JLabel lblId = new JLabel("");
		lblId.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblId.setBounds(172, 154, 38, 16);
		panelGenres.add(lblId);

// INSERT GENRE
		
		JButton buttonInsertGenre = new JButton("Insert");
		buttonInsertGenre.setFont(new Font("Tahoma", Font.BOLD, 20));
		buttonInsertGenre.setBounds(276, 254, 177, 50);
		panelGenres.add(buttonInsertGenre);
		buttonInsertGenre.addActionListener(new AddActionGenres());
		tableGenres.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
// SCROLLPANE TABLE GENRE	
		
		JScrollPane scrollPane_Genres = new JScrollPane(tableGenres);
		scrollPane_Genres.setBounds(181, 413, 392, 220);
		panelGenres.add(scrollPane_Genres);
		
			tableGenres.setModel(DBUtil.getAllGenres());
		
//insert text from table in textField		
		tableGenres.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent g) {
			
			conn = DBUtil.getConnected();
			if(conn == null) {
				return;
			}
			
			try{
				int row = tableGenres.getSelectedRow();
				String table_click = (tableGenres.getModel().getValueAt(row, 0).toString());
				String table_click1 = (tableGenres.getModel().getValueAt(row, 1).toString());
				
					lblId.setText(table_click);
					textField_Genres.setText(table_click1);
			
				tableGenres.setModel(DBUtil.getAllGenres());
			}catch(Exception em){
				JOptionPane.showMessageDialog(null,em);
			}
			
		}
		}); //end mouseListener
		

// DELETE GENRE
			JButton buttonDelete1 = new JButton("Delete");
			buttonDelete1.setFont(new Font("Tahoma", Font.BOLD, 16));
			buttonDelete1.setForeground(new Color(255, 0, 0));
			buttonDelete1.setBounds(287, 328, 152, 37);
			panelGenres.add(buttonDelete1);
			
				buttonDelete1.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					String sql ="delete from genres where genre_id=?";
				
						conn = DBUtil.getConnected();
						if(conn == null) {
						return;
						}
					try{
						state=conn.prepareStatement(sql);
						state.setString(1, lblId.getText().toString());
						state.execute();
						
						JOptionPane.showMessageDialog(null, "DELETED!");
						
						tableGenres.setModel(DBUtil.getAllGenres());
						tableMovies.setModel(DBUtil.getAllMovies());
						tableProjections.setModel(DBUtil.getAllProjections());
						
						FillComboGenre();
						FillComboSearchGenre();
					
					}catch(SQLException es){
						JOptionPane.showMessageDialog(null,"The genre bilongs to movie! Please delete movie before the genre.");
					}
				}
				
			});//end delete Genre
	
// UPDATE GENRE
			JButton btnUpdateGenre = new JButton("Update");
			btnUpdateGenre.setForeground(Color.BLUE);
			btnUpdateGenre.setFont(new Font("Tahoma", Font.BOLD, 16));
			btnUpdateGenre.setBounds(287, 203, 152, 38);
			panelGenres.add(btnUpdateGenre);
			
			btnUpdateGenre.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					String id = lblId.getText();
					String genre = textField_Genres.getText();
					
					conn = DBUtil.getConnected();
					if(conn == null) {
					return;
					}
					
				try{
					String sql ="update genres set genre='"+genre+"' where genre_id='"+id+"'";
					state=conn.prepareStatement(sql);
					state.execute();
					
					JOptionPane.showMessageDialog(null, "Updated!");
					
					tableGenres.setModel(DBUtil.getAllGenres());
					tableMovies.setModel(DBUtil.getAllMovies());
					
					FillComboGenre();
					FillComboSearchGenre();
				
				}catch(SQLException es){
					JOptionPane.showMessageDialog(null,es);
				}
					
				}
			});	
		
			
// PANEL 2 -------------------------------------------------------------------------------	
		JPanel panelMovies = new JPanel();
		panelMovies.setForeground(new Color(219, 112, 147));
		panelMovies.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Movies", null, panelMovies, null);
		tabbedPane.setBackgroundAt(1, new Color(139, 0, 139));
		panelMovies.setLayout(null);
		
		JLabel labelMolq2 = new JLabel("Enter movie!");
		labelMolq2.setBounds(280, 13, 214, 20);
		labelMolq2.setFont(new Font("Charlemagne Std", Font.BOLD, 19));
		panelMovies.add(labelMolq2);
		
		JLabel label_Name = new JLabel("Title :");
		label_Name.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_Name.setBounds(56, 64, 153, 16);
		panelMovies.add(label_Name);
		
		JLabel label_Rating = new JLabel("Genre:");
		label_Rating.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_Rating.setBounds(56, 126, 153, 16);
		panelMovies.add(label_Rating);
		
		JLabel label_DateReleace = new JLabel("Releace date :");
		label_DateReleace.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_DateReleace.setBounds(56, 196, 153, 16);
		panelMovies.add(label_DateReleace);
		
		JLabel label_Languege = new JLabel("Languege :");
		label_Languege.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_Languege.setBounds(56, 272, 153, 16);
		panelMovies.add(label_Languege);
		
		JLabel label_Subtitles = new JLabel("Subtitle :");
		label_Subtitles.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_Subtitles.setBounds(56, 349, 153, 16);
		panelMovies.add(label_Subtitles);
		
		JLabel label_Genre = new JLabel("Rating :");
		label_Genre.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_Genre.setBounds(57, 426, 106, 16);
		panelMovies.add(label_Genre);
		
		textField_Title = new JTextField();
		textField_Title.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField_Title.setBounds(253, 61, 253, 22);
		panelMovies.add(textField_Title);
		textField_Title.setColumns(10);
		
		comboBox_Genre = new JComboBox<String>();
		comboBox_Genre.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBox_Genre.setBounds(253, 123, 199, 22);
		panelMovies.add(comboBox_Genre);
			
		textField_Date = new JTextField();
		textField_Date.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField_Date.setBounds(253, 193, 153, 22);
		panelMovies.add(textField_Date);
		textField_Date.setColumns(10);
			
		textField_Languege = new JTextField();
		textField_Languege.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField_Languege.setBounds(253, 269, 89, 22);
		panelMovies.add(textField_Languege);
		textField_Languege.setColumns(10);
			
		textField_Subtitles = new JTextField();
		textField_Subtitles.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField_Subtitles.setBounds(253, 346, 89, 22);
		panelMovies.add(textField_Subtitles);
		textField_Subtitles.setColumns(10);
			
		textField_Rating = new JTextField();
		textField_Rating.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField_Rating.setBounds(253, 423, 89, 22);
		panelMovies.add(textField_Rating);
		textField_Rating.setColumns(10);
			
		JLabel lblYymmdd = new JLabel("/ yyyy-mm-dd /");
		lblYymmdd.setFont(new Font("Tahoma", Font.ITALIC, 14));
		lblYymmdd.setBounds(417, 197, 124, 16);
		panelMovies.add(lblYymmdd);
			
		JLabel label = new JLabel("/ 1-10 /");
		label.setFont(new Font("Tahoma", Font.ITALIC, 14));
		label.setBounds(354, 427, 56, 16);
		panelMovies.add(label);
			
		JLabel lblIdMovie = new JLabel("");
		lblIdMovie.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblIdMovie.setBounds(156, 38, 30, 16);
		panelMovies.add(lblIdMovie);
			
//INSERT MOVIE
					
		JButton buttonInsertMovie = new JButton("Insert");
		buttonInsertMovie.setFont(new Font("Tahoma", Font.BOLD, 20));
		buttonInsertMovie.setBounds(544, 312, 158, 63);
		panelMovies.add(buttonInsertMovie);
		buttonInsertMovie.addActionListener(new AddActionMovies());
	    tableMovies.setFont(new Font("Tahoma", Font.PLAIN, 15));
					
// SCROLLPANE TABLE PROJECTION		
					
		JScrollPane scrollPane_Movies = new JScrollPane(tableMovies);
		scrollPane_Movies.setBounds(30, 492, 708, 167);
		panelMovies.add(scrollPane_Movies);
		
			tableMovies.setModel(DBUtil.getAllMovies());
			
//insert text from table in textFields	
			tableMovies.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent m) {
				
		
				conn = DBUtil.getConnected();
				if(conn == null) {
					return;
				}
				
				try{
					int row = tableMovies.getSelectedRow();
					String table_click = (tableMovies.getModel().getValueAt(row, 0).toString());
					String table_click1 = (tableMovies.getModel().getValueAt(row, 1).toString());
					String table_click2 = (tableMovies.getModel().getValueAt(row, 2).toString());
					String table_click3 = (tableMovies.getModel().getValueAt(row, 3).toString());
					String table_click4 = (tableMovies.getModel().getValueAt(row, 4).toString());
					String table_click5 = (tableMovies.getModel().getValueAt(row, 5).toString());
					String table_click6 = (tableMovies.getModel().getValueAt(row, 6).toString());
					
						lblIdMovie.setText(table_click);
						textField_Title.setText(table_click1);
						comboBox_Genre.setSelectedItem(table_click2);
						textField_Date.setText(table_click3);
						textField_Languege.setText(table_click4);
						textField_Subtitles.setText(table_click5);
						textField_Rating.setText(table_click6);
				
					tableMovies.setModel(DBUtil.getAllMovies());
				}catch(Exception em){
					JOptionPane.showMessageDialog(null,em);
				}
			}
		}); //end mouseListener
			
// DELETE MOVIE
		JButton buttonDelete2 = new JButton("Delete");
		buttonDelete2.setFont(new Font("Tahoma", Font.BOLD, 16));
		buttonDelete2.setForeground(Color.RED);
		buttonDelete2.setBounds(555, 398, 133, 40);
		panelMovies.add(buttonDelete2);
						
			buttonDelete2.addActionListener(new ActionListener() {
							
			public void actionPerformed(ActionEvent e) {
				String sql ="delete from movies where movie_id=?";
								
					conn = DBUtil.getConnected();
					if(conn == null) {
					return;
								}
					try{
						state=conn.prepareStatement(sql);
						state.setString(1, lblIdMovie.getText().toString());
						state.execute();
									
						JOptionPane.showMessageDialog(null, "Deleted");
									
						tableMovies.setModel(DBUtil.getAllMovies());
						tableProjections.setModel(DBUtil.getAllProjections());
									
						FillComboMovie();
					}catch(Exception es){
						JOptionPane.showMessageDialog(null,"The movie bilongs to projection! Please delete projection before the movie.");
					}
					}
			}); // end Delete Movie
							
// UPDATE MOVIE
			JButton btnUpdateMovie = new JButton("Update");
			btnUpdateMovie.setForeground(Color.BLUE);
			btnUpdateMovie.setFont(new Font("Tahoma", Font.BOLD, 16));
			btnUpdateMovie.setBounds(555, 250, 133, 40);
			panelMovies.add(btnUpdateMovie);
			
			btnUpdateMovie.addActionListener(new ActionListener() {
				
				 public void actionPerformed(ActionEvent e) {
												
					String id = lblIdMovie.getText();
					String title = textField_Title.getText(); 
					String date = textField_Date.getText();
					String languege = textField_Languege.getText();
					String subtitle = textField_Subtitles.getText();
					String rating = textField_Rating.getText();
												
					conn = DBUtil.getConnected();
					if(conn == null) {
						return;
					}
												
					try{
						String sql ="update movies set title='"+title+"',releacedate='"+date+"',languege='"+languege+"',subtitles='"+subtitle+"',rating='"+rating+"' where movie_id='"+id+"'";
						state=conn.prepareStatement(sql);
						state.execute();
												
						JOptionPane.showMessageDialog(null, "Updated!");
												
						tableMovies.setModel(DBUtil.getAllMovies());
											
					}catch(SQLException es){
						JOptionPane.showMessageDialog(null,es);
					}
					}
		}); // end update
		
		
// PANEL 3 --------------------------------------------------------------		
		JPanel panelProjections = new JPanel();
		panelProjections.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Projections", null, panelProjections, null);
		tabbedPane.setBackgroundAt(2, new Color(0, 128, 0));
		panelProjections.setLayout(null);
		
		JLabel labelMolq3 = new JLabel("Create projection!");
		labelMolq3.setBounds(261, 13, 277, 20);
		labelMolq3.setFont(new Font("Charlemagne Std", Font.BOLD, 20));
		panelProjections.add(labelMolq3);
		
		JLabel label_Movie = new JLabel("Movie:");
		label_Movie.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_Movie.setBounds(70, 95, 103, 16);
		panelProjections.add(label_Movie);
		
		JLabel label_Hour = new JLabel("Projection time :");
		label_Hour.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_Hour.setBounds(70, 169, 163, 16);
		panelProjections.add(label_Hour);
		
		JLabel label_Number = new JLabel("Hall number :");
		label_Number.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_Number.setBounds(70, 244, 163, 16);
		panelProjections.add(label_Number);
		
		comboBox_Movie = new JComboBox<String>();
		comboBox_Movie.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBox_Movie.setBounds(304, 92, 251, 22);
		panelProjections.add(comboBox_Movie);
		
		textField_Hour = new JTextField();
		textField_Hour.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField_Hour.setBounds(304, 166, 116, 22);
		panelProjections.add(textField_Hour);
		textField_Hour.setColumns(10);
		
		textField_Hall = new JTextField();
		textField_Hall.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField_Hall.setBounds(304, 242, 116, 22);
		panelProjections.add(textField_Hall);
		textField_Hall.setColumns(10);
		
		JLabel lblHh = new JLabel("/ hh : mm /");
		lblHh.setFont(new Font("Tahoma", Font.ITALIC, 14));
		lblHh.setBounds(432, 170, 85, 16);
		panelProjections.add(lblHh);
		
		JLabel lblIdProjection = new JLabel("");
		lblIdProjection.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblIdProjection.setBounds(223, 55, 31, 16);
		panelProjections.add(lblIdProjection);
	
// INSERT PROJECTION
		JButton buttonAddProjections = new JButton("Insert");
		buttonAddProjections.setFont(new Font("Tahoma", Font.BOLD, 20));
		buttonAddProjections.setBounds(291, 343, 202, 60);
		panelProjections.add(buttonAddProjections);
		
			buttonAddProjections.addActionListener(new AddActionProjections());
			tableProjections.setFont(new Font("Tahoma", Font.PLAIN, 15));

// SCROLLPANE TABLE PROJECTION			
			JScrollPane scrollPane_Projections = new JScrollPane(tableProjections);
			scrollPane_Projections.setBounds(28, 465, 711, 194);
			panelProjections.add(scrollPane_Projections);
			
				tableProjections.setModel(DBUtil.getAllProjections());

//insert text from table in textFields	
				tableProjections.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent m) {
					
			
					conn = DBUtil.getConnected();
					if(conn == null) {
						return;
					}
					
					try{
						int row = tableProjections.getSelectedRow();
						String table_click = (tableProjections.getModel().getValueAt(row, 0).toString());
						String table_click1 = (tableProjections.getModel().getValueAt(row, 1).toString());
						String table_click2 = (tableProjections.getModel().getValueAt(row, 2).toString());
						String table_click3 = (tableProjections.getModel().getValueAt(row, 3).toString());
						
							lblIdProjection.setText(table_click);
							comboBox_Movie.setSelectedItem(table_click1);
							textField_Hour.setText(table_click2);
							textField_Hall.setText(table_click3);
					
						tableMovies.setModel(DBUtil.getAllMovies());
					}catch(Exception em){
						JOptionPane.showMessageDialog(null,em);
					}
				}
			}); //end mouseListener				
				
// DELETE PROJECTION
				JButton buttonDelete3 = new JButton("Delete");
				buttonDelete3.setFont(new Font("Tahoma", Font.BOLD, 16));
				buttonDelete3.setForeground(Color.RED);
				buttonDelete3.setBounds(523, 353, 179, 45);
				panelProjections.add(buttonDelete3);
				
				buttonDelete3.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						String sql ="delete from projections where projection_id=?";
						
							conn = DBUtil.getConnected();
							if(conn == null) {
							return;
							}
						try{
							state=conn.prepareStatement(sql);
							state.setString(1, lblIdProjection.getText().toString());
							state.execute();
							
							JOptionPane.showMessageDialog(null, "Deleted");
							
							tableProjections.setModel(DBUtil.getAllProjections());
						
						}catch(Exception es){
							JOptionPane.showMessageDialog(null,"Ooops!!!");
						}
					}
				}); //end delete projection

// UPDATE PROJECTION		
				
			JButton btnUpdateProjection = new JButton("Update");
			btnUpdateProjection.setForeground(Color.BLUE);
			btnUpdateProjection.setFont(new Font("Tahoma", Font.BOLD, 16));
			btnUpdateProjection.setBounds(76, 353, 178, 45);
			panelProjections.add(btnUpdateProjection);
			
			btnUpdateProjection.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					String id = lblIdProjection.getText();
					String timehour = textField_Hour.getText();
					String hall = textField_Hall.getText();
												
					conn = DBUtil.getConnected();
					if(conn == null) {
						return;
					}
												
					try{
						String sql ="update projections set time='"+timehour+"',hall='"+hall+"' where projection_id='"+id+"'";
						state=conn.prepareStatement(sql);
						state.execute();
												
						JOptionPane.showMessageDialog(null, "Updated!");
												
						tableProjections.setModel(DBUtil.getAllProjections());
											
					}catch(SQLException es){
						JOptionPane.showMessageDialog(null,"Update is wrong! Please enter valid data");
					}
				}
			}); // end update
			
			
// PANEL 4 ----------------------------------------------------------	
			JPanel panelFilters = new JPanel();
			panelFilters.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			tabbedPane.addTab("Search", null, panelFilters, null);
			tabbedPane.setBackgroundAt(3, new Color(224, 255, 255));
			panelFilters.setLayout(null);
			
// ______ GENADI SEARCH _______
			
			JLabel lblMovieLanguege = new JLabel("Movie languege :");
			lblMovieLanguege.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
			lblMovieLanguege.setBounds(77, 101, 166, 22);
			panelFilters.add(lblMovieLanguege);
			
			JLabel lblGenre_1 = new JLabel("Genre :");
			lblGenre_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
			lblGenre_1.setBounds(77, 48, 155, 22);
			panelFilters.add(lblGenre_1);
			
			textField_SearchLanguege = new JTextField();
			textField_SearchLanguege.setBounds(321, 103, 116, 22);
			panelFilters.add(textField_SearchLanguege);
			textField_SearchLanguege.setColumns(10);
			
			comboBoxGenres = new JComboBox<String>();
			comboBoxGenres.setToolTipText("");
			comboBoxGenres.setFont(new Font("Tahoma", Font.PLAIN, 15));
			comboBoxGenres.setBounds(321, 49, 166, 22);
			
			panelFilters.add(comboBoxGenres);
			
// SCROLLPANE TABLE SEARCH				
			JScrollPane scrollPaneSearch1 = new JScrollPane(tableSearch1);
			scrollPaneSearch1.setBounds(25, 176, 717, 158);
			panelFilters.add(scrollPaneSearch1);
			
				tableSearch1.setModel(DBUtil.getAllSearch1("", ""));
			
// BUTTON SEARCH				
			JButton btnSearch1 = new JButton("Search");
			btnSearch1.setFont(new Font("Tahoma", Font.BOLD, 18));
			btnSearch1.setForeground(Color.ORANGE);
			btnSearch1.setBounds(520, 97, 116, 34);
			panelFilters.add(btnSearch1);
			
			btnSearch1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					String genre =comboBoxGenres.getItemAt(comboBoxGenres.getSelectedIndex());
					String languege = textField_SearchLanguege.getText();
						
					tableSearch1.setModel(DBUtil.getAllSearch1(genre, languege));
				}
			}); // end search
			
// ________ PRESLAV SEARCH _________			
			
			JLabel lblRating = new JLabel("Movie rating :");
			lblRating.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
			lblRating.setBounds(77, 372, 155, 22);
			panelFilters.add(lblRating);
			
			JLabel lblProjectionHall = new JLabel("Projection hall :");
			lblProjectionHall.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
			lblProjectionHall.setBounds(77, 432, 155, 28);
			panelFilters.add(lblProjectionHall);
			
			textField_SearchRating = new JTextField();
			textField_SearchRating.setBounds(321, 374, 116, 22);
			panelFilters.add(textField_SearchRating);
			textField_SearchRating.setColumns(10);
			
			
			textField_SearchHall = new JTextField();
			textField_SearchHall.setBounds(321, 437, 116, 22);
			panelFilters.add(textField_SearchHall);
			textField_SearchHall.setColumns(10);

// SCROLLPANE TABLE SEARCH			
			JScrollPane scrollPaneSearch2 = new JScrollPane(tableSearch2);
			scrollPaneSearch2.setBounds(25, 496, 717, 163);
			panelFilters.add(scrollPaneSearch2);
						
				tableSearch2.setModel(DBUtil.getAllSearch2(0,0));
			
// BUTTON SEARCH			
			JButton btnSearch2 = new JButton("Search");
			btnSearch2.setForeground(SystemColor.textHighlight);
			btnSearch2.setFont(new Font("Tahoma", Font.BOLD, 18));
			btnSearch2.setBounds(520, 402, 116, 34);
			panelFilters.add(btnSearch2);
			
			btnSearch2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					

					int rating = Integer.parseInt(textField_SearchRating.getText().toString());
					int hall = Integer.parseInt(textField_SearchHall.getText().toString());
					
					tableSearch2.setModel(DBUtil.getAllSearch2(rating,hall));		
				}
			}); //end search				
		
// VOID METHODS
		FillComboGenre();
		FillComboMovie();
		FillComboSearchGenre();
	}

// ------------------------------------------- END CONSTRUCTOR -----------------------------------------------------------------------

// INSERTS 
class AddActionGenres implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		String genre = textField_Genres.getText();
		
		conn = DBUtil.getConnected();
		if(conn == null) {
			return;
		}
		String sql ="insert into genres values (null,?);";
		
		try {
			state = conn.prepareStatement(sql);
			state.setString(1, genre);
			state.execute();
			
			tableGenres.setModel(DBUtil.getAllGenres());
			
			FillComboGenre();
			FillComboSearchGenre();
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
	}
  }//end AddActionGenres

class AddActionMovies implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		String name = textField_Title.getText();
		String genre = comboBox_Genre.getItemAt(comboBox_Genre.getSelectedIndex());
		String date = textField_Date.getText();
		String languege = textField_Languege.getText();
		String subtitles = textField_Subtitles.getText();
		int rating = 0;
		try {
			rating = Integer.parseInt(textField_Rating.getText());
		}catch(NumberFormatException ex) {
			JOptionPane.showMessageDialog(null,"Enter only numbers!");
		}
		
		
		conn = DBUtil.getConnected();
		if(conn == null) {
			return;
		}
		String sql ="insert into movies values (null,?,(select genre_id from genres where genre=?),?,?,?,?);";
		
		try {
			state = conn.prepareStatement(sql);
			state.setString(1, name);
			state.setString(2, genre);
			state.setString(3, date);
			state.setString(4, languege);
			state.setString(5, subtitles);
			state.setInt(6, rating);
			state.execute();
			
			tableMovies.setModel(DBUtil.getAllMovies());
			FillComboMovie();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
	}	
  }//end AddActionMovies

class AddActionProjections implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		String movie = comboBox_Movie.getItemAt(comboBox_Movie.getSelectedIndex());
		String time = textField_Hour.getText();
		int hall = 0;
		try {
			hall = Integer.parseInt(textField_Hall.getText());
		}catch(NumberFormatException ex) {
			textField_Hall.setText("Enter only numbers!");
		}
		
			conn = DBUtil.getConnected();
			if(conn == null) {
			return;
			}
		String sql ="insert into projections values (null,(select movie_id from movies where title=?),?,?);";
		
		try {
			state = conn.prepareStatement(sql);
			state.setString(1, movie);
			state.setString(2, time);
			state.setInt(3, hall);
			state.execute();
			
			tableProjections.setModel(DBUtil.getAllProjections());
		} catch (SQLException e3) {
			e3.printStackTrace();
		}
	}
  }//end AddActionProjections

// END INSERTS 

public void FillComboGenre(){
		
	conn = DBUtil.getConnected();
	if(conn == null) {
		return;
	}
	try{
		String sql = "select genre from genres";
		state = conn.prepareStatement(sql);
		rs = state.executeQuery();				
		comboBox_Genre.removeAllItems();
		comboBox_Genre.addItem("select item");
		while(rs.next()){
			comboBox_Genre.addItem(rs.getString("genre"));
		}
			
	}catch(Exception e5) {
		JOptionPane.showMessageDialog(null,e5);
	}
}//end FillComboBox_Genre

public void FillComboMovie(){
	
	conn = DBUtil.getConnected();
	if(conn == null) {
		return;
	}
	
	try{
		String sql = "select title from movies";
		state = conn.prepareStatement(sql);
		rs = state.executeQuery();				
		comboBox_Movie.removeAllItems();
		comboBox_Movie.addItem("select item");
		while(rs.next()){
			comboBox_Movie.addItem(rs.getString("title"));
		}
		
	}catch(Exception e5) {
		JOptionPane.showMessageDialog(null,e5);
	}
}//end FillComboBox_Movie

public void FillComboSearchGenre(){
	
	conn = DBUtil.getConnected();
	if(conn == null) {
		return;
	}
	try{
		String sql = "select genre from genres";
		state = conn.prepareStatement(sql);
		rs = state.executeQuery();				
		comboBoxGenres.removeAllItems();
		comboBoxGenres.addItem("select item");
		while(rs.next()){
			comboBoxGenres.addItem(rs.getString("genre"));
		}
			
	}catch(Exception e5) {
		JOptionPane.showMessageDialog(null,e5);
	}
}//end FillComboBoxSearhgenre

}//end class CINEMA
