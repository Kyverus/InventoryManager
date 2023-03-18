package inventoryManager;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.proteanit.sql.DbUtils;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.sql.*;
import javax.swing.JTable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.CardLayout;

public class Main {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		connect();
		initialize();
		loadTable();
	}
	
	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	
	private JTextField addNameField;
	private JTextField addCategoryField;
	private JTextField addPriceField;
	private JTable viewItemsTable;
	private JTextField editNameField;
	private JTextField editCategoryField;
	private JTextField editPriceField;
	private JTextField searchIDField;
	
	public void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/crudproject", "root", "");
		}catch (ClassNotFoundException ex){
			
		}catch (SQLException ex){
			JOptionPane.showMessageDialog(null, ex, "Database Unreachable!", 0);
			System.exit(0);
		}
	}
	
	void loadTable() {
		try {
			pst = con.prepareStatement("SELECT * FROM items");
			rs = pst.executeQuery();
			viewItemsTable.setModel(DbUtils.resultSetToTableModel(rs));
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(37, 37, 37));
		frame.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 11));
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		
		DragListener drag = new DragListener();
		frame.addMouseListener( drag );
		frame.addMouseMotionListener( drag );
		frame.getContentPane().setLayout(null);
		
		JPanel cardLayoutPanel = new JPanel();
		cardLayoutPanel.setBounds(210, 11, 680, 578);
		frame.getContentPane().add(cardLayoutPanel);
		
		CardLayout cl = new CardLayout();
		cardLayoutPanel.setLayout(cl);
		
		JPanel viewPanel = new JPanel();
		viewPanel.setBackground(Color.DARK_GRAY);
		cardLayoutPanel.add(viewPanel, "1");
		viewPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 45, 660, 522);
		viewPanel.add(scrollPane);
		
		viewItemsTable = new JTable();
		viewItemsTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setViewportView(viewItemsTable);
		
		JLabel lblNewLabel = new JLabel("ITEMS TABLE");
		lblNewLabel.setFont(new Font("Franklin Gothic Book", Font.BOLD, 20));
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(276, 11, 128, 14);
		viewPanel.add(lblNewLabel);
		
		JPanel addPanel = new JPanel();
		addPanel.setBackground(Color.DARK_GRAY);
		cardLayoutPanel.add(addPanel, "2");
		addPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 45, 660, 225);
		addPanel.add(panel);
		panel.setLayout(null);
		
		JLabel addNameLabel = new JLabel("Name");
		addNameLabel.setBounds(34, 62, 68, 14);
		panel.add(addNameLabel);
		
		JLabel addCategoryLabel = new JLabel("Category");
		addCategoryLabel.setBounds(34, 106, 68, 14);
		panel.add(addCategoryLabel);
		
		JLabel addPriceLabel = new JLabel("Price");
		addPriceLabel.setBounds(34, 153, 68, 14);
		panel.add(addPriceLabel);
		
		addNameField = new JTextField();
		addNameField.setBounds(112, 59, 255, 20);
		panel.add(addNameField);
		addNameField.setColumns(10);
		
		addCategoryField = new JTextField();
		addCategoryField.setColumns(10);
		addCategoryField.setBounds(112, 103, 255, 20);
		panel.add(addCategoryField);
		
		addPriceField = new JTextField();
		addPriceField.setColumns(10);
		addPriceField.setBounds(112, 150, 255, 20);
		panel.add(addPriceField);
		
		JButton clearButton = new JButton("Clear");
		clearButton.setBounds(470, 125, 122, 42);
		panel.add(clearButton);
		
		JButton saveItemButton = new JButton("Save Item");
		saveItemButton.setBounds(470, 62, 122, 42);
		panel.add(saveItemButton);
		
		JLabel lblAddItem = new JLabel("ADD ITEM");
		lblAddItem.setForeground(Color.WHITE);
		lblAddItem.setFont(new Font("Franklin Gothic Book", Font.BOLD, 20));
		lblAddItem.setBounds(288, 11, 103, 14);
		addPanel.add(lblAddItem);
		saveItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String iname,icategory,iprice;
				iname = addNameField.getText();
				icategory = addCategoryField.getText();
				iprice = addPriceField.getText();
				
				try {
					pst = con.prepareStatement("INSERT INTO items (name,category,price) VALUES (?,?,?)");
					pst.setString(1, iname);
					pst.setString(2, icategory);
					pst.setString(3, iprice);
					pst.executeUpdate();
					
					JOptionPane.showMessageDialog(null, "Item Added!");
					loadTable();
					          
					addNameField.setText("");
					addCategoryField.setText("");
					addPriceField.setText("");
					addNameField.requestFocus();
					
				}catch (SQLException e1){
					e1.printStackTrace();
				}
			}
		});
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNameField.setText("");
				addCategoryField.setText("");
				addPriceField.setText("");
				addNameField.requestFocus();
			}
		});
		
		JPanel editPanel = new JPanel();
		editPanel.setBackground(Color.DARK_GRAY);
		cardLayoutPanel.add(editPanel, "3");
		editPanel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(10, 121, 660, 225);
		editPanel.add(panel_1);
		
		JLabel editNameLabel = new JLabel("Name");
		editNameLabel.setBounds(34, 62, 68, 14);
		panel_1.add(editNameLabel);
		
		JLabel editCategoryLabel = new JLabel("Category");
		editCategoryLabel.setBounds(34, 106, 68, 14);
		panel_1.add(editCategoryLabel);
		
		JLabel editPriceLabel = new JLabel("Price");
		editPriceLabel.setBounds(34, 153, 68, 14);
		panel_1.add(editPriceLabel);
		
		editNameField = new JTextField();
		editNameField.setColumns(10);
		editNameField.setBounds(112, 59, 255, 20);
		panel_1.add(editNameField);
		
		editCategoryField = new JTextField();
		editCategoryField.setColumns(10);
		editCategoryField.setBounds(112, 103, 255, 20);
		panel_1.add(editCategoryField);
		
		editPriceField = new JTextField();
		editPriceField.setColumns(10);
		editPriceField.setBounds(112, 150, 255, 20);
		panel_1.add(editPriceField);
		
		JButton updateItemButton = new JButton("Update Item");
		updateItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String iname,icategory,iprice,iid;
				iname = editNameField.getText();
				icategory = editCategoryField.getText();
				iprice = editPriceField.getText();
				iid  = searchIDField.getText();
				
				try {
					pst = con.prepareStatement("UPDATE items SET name = ?,category = ?,price = ? WHERE id = ?");
					pst.setString(1, iname);
		            pst.setString(2, icategory);
		            pst.setString(3, iprice);
		            pst.setString(4, iid);
		            pst.executeUpdate();
		            JOptionPane.showMessageDialog(null, "Item Updated!");
		            loadTable();
		          
		            editNameField.setText("");
					editCategoryField.setText("");
					editPriceField.setText("");
					editNameField.requestFocus();
				}catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		updateItemButton.setBounds(474, 62, 118, 39);
		panel_1.add(updateItemButton);
		
		JButton deleteItemButton = new JButton("Delete Item");
		deleteItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                String iid;
				iid  = searchIDField.getText();
				try {
					pst = con.prepareStatement("DELETE FROM items WHERE id = ?");
				    pst.setString(1, iid);
				    pst.executeUpdate();
				    JOptionPane.showMessageDialog(null, "Item Deleted!");
				    loadTable();
				  
				    editNameField.setText("");
					editCategoryField.setText("");
					editPriceField.setText("");
					editNameField.requestFocus();
					
				}catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		deleteItemButton.setBounds(474, 128, 118, 39);
		panel_1.add(deleteItemButton);
		
		JLabel lblEditItem = new JLabel("EDIT ITEM");
		lblEditItem.setForeground(Color.WHITE);
		lblEditItem.setFont(new Font("Franklin Gothic Book", Font.BOLD, 20));
		lblEditItem.setBounds(288, 11, 103, 14);
		editPanel.add(lblEditItem);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 45, 287, 56);
		editPanel.add(panel_2);
		panel_2.setBorder(new TitledBorder(null, "Search by ID", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setLayout(null);
		
		searchIDField = new JTextField();
		searchIDField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
			          
		            String id = searchIDField.getText();
		 
		                pst = con.prepareStatement("SELECT name,category,price FROM items WHERE id = ?");
		                pst.setString(1, id);
		                ResultSet rs = pst.executeQuery();
		 
		            if(rs.next()==true) {
		              
		                String name = rs.getString(1);
		                String category = rs.getString(2);
		                String price = rs.getString(3);
		                
		                editNameField.setText(name);
		                editCategoryField.setText(category);
		                editPriceField.setText(price);
		                
		            }else {
		            	editNameField.setText("");
		            	editCategoryField.setText("");
		            	editPriceField.setText("");
		            }
		        }catch (SQLException ex) {
		          
		        }
			}
		});
		searchIDField.setColumns(10);
		searchIDField.setBounds(53, 18, 180, 20);
		panel_2.add(searchIDField);
		
		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(new Color(245, 210, 90));
		menuPanel.setBounds(10, 11, 190, 578);
		frame.getContentPane().add(menuPanel);
		menuPanel.setLayout(null);
		
		JButton addPanelButton = new JButton("Insert Item");
		addPanelButton.setForeground(Color.WHITE);
		addPanelButton.setBackground(new Color(37, 37, 37));
		addPanelButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		addPanelButton.setBorderPainted(false);
		addPanelButton.setFocusPainted(false);
		addPanelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(cardLayoutPanel, "2");
			}
		});
		addPanelButton.setBounds(28, 39, 134, 37);
		menuPanel.add(addPanelButton);
		
		JButton viewPanelButton = new JButton("View Items");
		viewPanelButton.setForeground(Color.WHITE);
		viewPanelButton.setBackground(new Color(37, 37, 37));
		viewPanelButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		viewPanelButton.setBorderPainted(false);
		viewPanelButton.setFocusPainted(false);
		viewPanelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(cardLayoutPanel, "1");
			}
		});
		viewPanelButton.setBounds(28, 145, 134, 37);
		menuPanel.add(viewPanelButton);
		
		JButton editPanelButton = new JButton("Edit Item");
		editPanelButton.setBackground(new Color(37, 37, 37));
		editPanelButton.setForeground(Color.WHITE);
		editPanelButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		editPanelButton.setBorderPainted(false);
		editPanelButton.setFocusPainted(false);
		editPanelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(cardLayoutPanel, "3");
			}
		});
		editPanelButton.setBounds(28, 265, 134, 37);
		menuPanel.add(editPanelButton);
		
		JButton exitButton = new JButton("EXIT");
		exitButton.setForeground(Color.WHITE);
		exitButton.setBackground(new Color(37, 37, 37));
		exitButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		exitButton.setBorderPainted(false);
		exitButton.setFocusPainted(false);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitButton.setBounds(28, 544, 134, 23);
		menuPanel.add(exitButton);
		
	}
}
