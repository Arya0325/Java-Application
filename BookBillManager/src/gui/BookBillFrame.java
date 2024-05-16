package gui;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import model.Book;
import utils.BookManager;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;

public class BookBillFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JTable table;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private DefaultTableModel model;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private int bookNumber = 1;
	private double totalAmount = 0.0, offerPrice = 0.0, payableAmount = 0.0;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_10;
	private String deliveryText;


	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				BookBillFrame frame = new BookBillFrame();
				frame.setVisible(true);
				frame.setTitle("pravda books");
				ImageIcon icon = new ImageIcon("C:\\pravdaBooksBillManagement\\frameicon.png"); // Change
																								// "path_to_your_image.jpg"
																								// to the actual path of
																								// your image file
				frame.setIconImage(icon.getImage());

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the frame.
	 */

	public BookBillFrame() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				updateBookStock(); // Call method to restore stock
			}

			private void updateBookStock() {
				List<Book> books = BookManager.readBooksFromFile();
				for (int i = 0; i < model.getRowCount(); i++) {
					String bookName = (String) model.getValueAt(i, 1); // Assuming book name is in column 1
					int quantity = (Integer) model.getValueAt(i, 3); // Assuming quantity is in column 3

					for (Book book : books) {
						if (book.getName().equals(bookName)) {
							int oldStock = book.getQuantity();
							book.setQuantity(oldStock + quantity); // Restore the removed quantity
							break;
						}
					}
				}
				updateBooksFile(books); // Write the updated list of books back to the file
			}

			private void updateBooksFile(List<Book> books) {
				String filePath = "C:\\pravdaBooksBillManagement\\books.txt";
				ensureDirectoryExists(filePath); // Ensure directory exists before writing

				try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
					for (Book book : books) {
						writer.println(
								book.getNo() + "," + book.getName() + "," + book.getRate() + "," + book.getQuantity());
					}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Failed to write to book file. Error: " + e.getMessage(),
							"File Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}

			private void ensureDirectoryExists(String filePath) {
				File file = new File(filePath);
				File parentDirectory = file.getParentFile();
				if (!parentDirectory.exists()) {
					parentDirectory.mkdirs(); // Make sure the directory exists
				}
			}

		});

		setSize(1200, 700); // Set the size explicitly

		BufferedImage backgroundImage;
		try {
			backgroundImage = ImageIO.read(new File("C:\\pravdaBooksBillManagement\\billinerfaceimg.jpg")); // Specify
																											// your
																											// image
			// path here
		} catch (IOException e) {
			e.printStackTrace();
			backgroundImage = null; // Handle potential IOException if image not found
		}
		final BufferedImage finalBackgroundImage = backgroundImage; // Effectively final for use in inner class
		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (finalBackgroundImage != null) {
					g.drawImage(finalBackgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
				}
			}
		};

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");

		LocalDate currentDate = LocalDate.now(); // Get the current date
		LocalTime currentTime = LocalTime.now(); // Get the current time

		List<Book> books = BookManager.readBooksFromFile();
		String[] columnNames = { "No", "Book Name", "Rate", "Quantity", "Total" };
		model = new DefaultTableModel(columnNames, 0);
		contentPane.setLayout(null);
		contentPane.setLayout(null);
		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(119, 222, 840, 284);
		contentPane.add(scrollPane);

		JLabel lblNewLabel = new JLabel("Date");
		lblNewLabel.setForeground(new Color(0, 0, 128));
		lblNewLabel.setBounds(990, 74, 34, 17);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Time");
		lblNewLabel_1.setForeground(new Color(0, 0, 128));
		lblNewLabel_1.setBounds(990, 109, 35, 17);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Name and Address");
		lblNewLabel_2.setForeground(new Color(0, 0, 128));
		lblNewLabel_2.setBounds(244, 96, 195, 18);
		lblNewLabel_2.setFont(new Font("Dialog", Font.BOLD, 13));
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Books");
		lblNewLabel_3.setForeground(new Color(0, 0, 128));
		lblNewLabel_3.setBounds(220, 157, 42, 18);
		lblNewLabel_3.setFont(new Font("Dialog", Font.BOLD, 12));
		contentPane.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Rate");
		lblNewLabel_4.setForeground(new Color(0, 0, 128));
		lblNewLabel_4.setBounds(462, 157, 28, 18);
		lblNewLabel_4.setFont(new Font("Dialog", Font.BOLD, 12));
		contentPane.add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("Stock");
		lblNewLabel_5.setForeground(new Color(0, 0, 128));
		lblNewLabel_5.setBounds(657, 157, 35, 18);
		lblNewLabel_5.setFont(new Font("Dialog", Font.BOLD, 12));
		contentPane.add(lblNewLabel_5);

		JLabel lblNewLabel_6 = new JLabel("Quantity");
		lblNewLabel_6.setForeground(new Color(0, 0, 128));
		lblNewLabel_6.setBounds(868, 157, 60, 18);
		lblNewLabel_6.setFont(new Font("Dialog", Font.BOLD, 12));
		contentPane.add(lblNewLabel_6);

		JLabel lblNewLabel_7 = new JLabel("Discount");
		lblNewLabel_7.setForeground(new Color(0, 0, 128));
		lblNewLabel_7.setBounds(185, 523, 80, 18);
		lblNewLabel_7.setFont(new Font("Dialog", Font.BOLD, 12));
		contentPane.add(lblNewLabel_7);

		textField = new JTextField(currentDate.format(dateFormatter));
		textField.setBounds(1056, 75, 96, 19);
		contentPane.add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField(currentTime.format(timeFormatter));
		textField_1.setBounds(1056, 110, 96, 19);
		contentPane.add(textField_1);
		textField_1.setColumns(10);

		textField_2 = new JTextField();
		textField_2.setForeground(new Color(0, 0, 128));
		textField_2.setBounds(244, 115, 628, 29);
		contentPane.add(textField_2);
		textField_2.setColumns(10);

		textField_3 = new JTextField();
		textField_3.setBackground(new Color(255, 255, 255));
		textField_3.setForeground(new Color(0, 0, 0));
		textField_3.setBounds(431, 178, 96, 19);
		contentPane.add(textField_3);
		textField_3.setColumns(10);

		textField_6 = new JTextField();
		textField_6.setBounds(630, 178, 96, 19);
		contentPane.add(textField_6);
		textField_6.setColumns(10);

		textField_7 = new JTextField();
		textField_7.setBounds(847, 178, 96, 19);
		contentPane.add(textField_7);
		textField_7.setColumns(10);

		textField_8 = new JTextField();
		textField_8.setBounds(166, 544, 96, 29);
		contentPane.add(textField_8);
		textField_8.setColumns(10);

		textField_9 = new JTextField();
		textField_9.setForeground(new Color(0, 0, 128));
		textField_9.setBackground(new Color(255, 255, 255));
		textField_9.setBounds(657, 539, 180, 25);
		contentPane.add(textField_9);
		textField_9.setColumns(10);

		textField_4 = new JTextField();
		textField_4.setForeground(new Color(0, 0, 128));
		textField_4.setBackground(new Color(255, 255, 255));
		textField_4.setBounds(657, 569, 180, 22);
		contentPane.add(textField_4);
		textField_4.setColumns(10);

		textField_5 = new JTextField();
		textField_5.setForeground(new Color(0, 0, 128));
		textField_5.setBackground(new Color(255, 255, 255));
		textField_5.setBounds(657, 596, 180, 25);
		contentPane.add(textField_5);
		textField_5.setColumns(10);
		
		textField_10 = new JTextField();
		textField_10.setBounds(312, 544, 104, 29);
		contentPane.add(textField_10);
		textField_10.setColumns(10);

		JComboBox<String> comboBox = new JComboBox<>(); // Assuming book names are strings
		comboBox.setBounds(134, 177, 221, 20);
		comboBox.addItem("Select"); // Default item
		for (Book book : books) {
			comboBox.addItem(book.getName());
		}

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedBook = (String) comboBox.getSelectedItem();

				if (selectedBook != null && !selectedBook.equals("Select")) {
					for (Book book : books) {
						if (book.getName().equals(selectedBook)) {
							textField_3.setText(String.valueOf(book.getRate()));
							textField_6.setText(String.valueOf(book.getQuantity()));
							break;
						}
					}
				} else {
					textField_3.setText("");
					textField_6.setText("");
				}

			}
		});
		contentPane.add(comboBox);

		JButton btnNewButton = new JButton("+ Add");
		btnNewButton.setForeground(new Color(0, 128, 0));
		btnNewButton.setBounds(990, 267, 126, 36);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAddBookAction();
				updateBooksFile();
				renumberBookEntries();
			}
			private void renumberBookEntries() {
			    int rowCount = model.getRowCount();
			    // Renumber existing book numbers
			    for (int i = 0; i < rowCount; i++) {
			        model.setValueAt(i + 1, i, 0); // Assuming book number is in the first column
			        books.get(i).setNo(i + 1); // Update book number in the books list
			    }
			}
			private void handleAddBookAction() {
				if (comboBox.getSelectedIndex() > 0) {
					int selectedIndex = comboBox.getSelectedIndex() - 1;
					Book selectedBook = books.get(selectedIndex);

					try {
						double rate = Double.parseDouble(textField_3.getText().trim());
						int quantity = Integer.parseInt(textField_7.getText().trim());
						int stock = selectedBook.getQuantity();

						if (quantity > stock) {
							JOptionPane.showMessageDialog(BookBillFrame.this, "Quantity exceeds stock available!",
									"Stock Error", JOptionPane.ERROR_MESSAGE);
						} else {
							selectedBook.setQuantity(stock - quantity); // Update stock

							double total = rate * quantity;

							model.addRow(new Object[] { bookNumber++, selectedBook.getName(), rate, quantity, total });

							updateBooksFile(); // Ensure this is called
							resetInputFields();
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(BookBillFrame.this, "Please enter quantity.", "Input Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(BookBillFrame.this, "Please select a book.", "Selection Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}

			private void resetInputFields() {
				textField_3.setText(""); // Clears the rate field
				textField_7.setText(""); // Clears the quantity field
				textField_6.setText(""); // Optionally clears the stock field if it is editable
				comboBox.setSelectedIndex(0);
			}

			private void ensureDirectoryExists(String filePath) {
				File file = new File(filePath);
				File parentDirectory = file.getParentFile();
				if (!parentDirectory.exists()) {
					parentDirectory.mkdirs(); // Make sure the directory exists
				}
			}

			private void updateBooksFile() {
				String filePath = "C:\\pravdaBooksBillManagement\\books.txt";
				ensureDirectoryExists(filePath); // Ensure directory exists before writing

				try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
					for (Book book : books) {
						writer.println(
								book.getNo() + "," + book.getName() + "," + book.getRate() + "," + book.getQuantity());
					}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Failed to write to book file. Error: " + e.getMessage(),
							"File Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}

		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("- Remove");
		btnNewButton_1.setForeground(new Color(159, 38, 56));
		btnNewButton_1.setBounds(990, 312, 126, 36);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				deleteSelectedRow();
			}

			private void deleteSelectedRow() {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					String bookName = (String) model.getValueAt(selectedRow, 1); // Assuming book name is in column 1
					int quantity = (Integer) model.getValueAt(selectedRow, 3); // Assuming quantity is in column 3

					for (Book book : books) {
						if (book.getName().equals(bookName)) {
							book.setQuantity(book.getQuantity() + quantity); // Restock the removed quantity
							break;
						}
					}

					model.removeRow(selectedRow);

					// Check if the deleted row was the last row
					if (selectedRow == model.getRowCount()) {
						// Reset book numbers starting from 1
						for (int i = 0; i < books.size(); i++) {
							books.get(i).setNo(i + 1);
						}
					} else {
						// If the deleted row was not the last row, renumber book entries
						renumberBookEntries();
					}

					updateBooksFile();

					JOptionPane.showMessageDialog(null, "Book removed and stock updated.", "Success",
							JOptionPane.INFORMATION_MESSAGE);
					if (model.getRowCount() == 0) {
						resetBookNumber(); // Reset book numbers if all books are removed
					}

				} else {
					JOptionPane.showMessageDialog(null, "Please select a row to delete.", "Selection Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}

			private void resetBookNumber() {
				bookNumber = 1;

			}

			private void updateBooksFile() {
				String filePath = "C:\\pravdaBooksBillManagement\\books.txt";
				ensureDirectoryExists(filePath); // Ensure directory exists before writing

				try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
					for (Book book : books) {
						writer.println(
								book.getNo() + "," + book.getName() + "," + book.getRate() + "," + book.getQuantity());
					}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Failed to write to book file. Error: " + e.getMessage(),
							"File Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}

			private void ensureDirectoryExists(String filePath) {
				File file = new File(filePath);
				File parentDirectory = file.getParentFile();
				if (!parentDirectory.exists()) {
					parentDirectory.mkdirs(); // Make sure the directory exists
				}
			}

			private void renumberBookEntries() {
			    int rowCount = model.getRowCount();
			    // Renumber existing book numbers
			    for (int i = 0; i < rowCount; i++) {
			        model.setValueAt(i + 1, i, 0); // Assuming book number is in the first column
			        books.get(i).setNo(i + 1); // Update book number in the books list
			    }
			}


		});

		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		contentPane.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("GrantTotal");
		btnNewButton_2.setForeground(new Color(0, 0, 128));
		btnNewButton_2.setBounds(462, 540, 153, 36);
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				updateTotal(); // Update total from the table model
				calculateAndDisplayOfferPrice(); // Calculate and display the offer price
				calculateAndDisplayPayableAmount();
				// Calculate and display the payable amount
			}

			private void updateTotal() {
				totalAmount = 0.0; // Reset total
				for (int i = 0; i < model.getRowCount(); i++) {
					Object value = model.getValueAt(i, 4); // Assume total is in the 5th column
					if (value instanceof Number) {
						totalAmount += ((Number) value).doubleValue();
					}
				}
				textField_9.setText("Total: " + String.format("%.2f\n", totalAmount));
			}

			private void calculateAndDisplayOfferPrice() {

				String discountText = textField_8.getText().trim(); // Get the text from the discount field
				if (discountText.isEmpty()) {
					offerPrice = 0;
				} else {
					try {
						double discountPercent = Double.parseDouble(discountText);
						offerPrice = totalAmount * (discountPercent / 100);
					} catch (NumberFormatException ex) {
						textField_4.setText("Offer Price: Invalid discount format.\n");
						return;
					}
				}
				textField_4.setText("Discount: " + String.format("%.2f\n", offerPrice)); // Clear previous text
			}

			private void calculateAndDisplayPayableAmount() {
			    int deliveryCharge = 0; // Initialize delivery charge to 0
			    String deliveryText = textField_10.getText().trim(); // Get delivery charge from textField_10 and remove leading/trailing whitespace
			    if (!deliveryText.isEmpty()) { // Check if the input is not empty
			        try {
			            deliveryCharge = Integer.parseInt(deliveryText); // Parse the delivery charge
			        } catch (NumberFormatException ex) {
			            // Handle invalid input
			            JOptionPane.showMessageDialog(null, "Invalid delivery charge format.", "Error", JOptionPane.ERROR_MESSAGE);
			            ex.printStackTrace();
			            return; // Exit the method if parsing fails
			        }
			    }

			    // Check if delivery charge is 0 or empty
			    if (deliveryCharge == 0 && deliveryText.isEmpty()) {
			        payableAmount = totalAmount - offerPrice; // Calculate payable amount without including delivery charge
			    } else {
			        payableAmount = totalAmount - offerPrice + deliveryCharge; // Include delivery charge in the payable amount
			    }

			    textField_5.setText("Payable Amount: " + String.format("%.2f\n", payableAmount)); // Update payable amount display
			}



		});
		contentPane.add(btnNewButton_2);
		// Assuming textField_4, textField_5, and textField_9 are JTextField objects.
		if (textField_4 != null) {
			Font textField_4boldFont = new Font(textField_4.getFont().getName(), Font.BOLD,
					textField_4.getFont().getSize());
			textField_4.setFont(textField_4boldFont);
		}

		if (textField_5 != null) {
			Font textField_5boldFont = new Font(textField_5.getFont().getName(), Font.BOLD,
					textField_5.getFont().getSize());
			textField_5.setFont(textField_5boldFont);
		}

		if (textField_9 != null) {
			Font textField_9boldFont = new Font(textField_9.getFont().getName(), Font.BOLD,
					textField_9.getFont().getSize());
			textField_9.setFont(textField_9boldFont);
		}

		JButton btnNewButton_3 = new JButton("GenerateBill");
		btnNewButton_3.setBackground(new Color(175, 3, 3));
		btnNewButton_3.setForeground(new Color(255, 255, 255));
		btnNewButton_3.setBounds(928, 555, 153, 44);
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateTotal();
				calculateAndDisplayOfferPrice();
				calculateAndDisplayPayableAmount();
				 deliveryText = textField_10.getText().trim();

				// Get customer name and sanitize it for filename usage
				String customerName = textField_2.getText().trim();
				if (customerName.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Customer name is empty", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String safeCustomerName = customerName.replaceAll("[^a-zA-Z0-9\\-]", "_");

				// Define date and time formats for the filename
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh-mm-ss");

				// Current date and time
				LocalDate currentDate = LocalDate.now();
				LocalTime currentTime = LocalTime.now();
				String formattedDate = currentDate.format(dateFormatter);
				String formattedTime = currentTime.format(timeFormatter);

				// Construct the directory path and filename
				String directoryPath = "C:\\PravdaBooksBills"; // Directory path
				String fileName = String.format("%s.%s_%s.pdf", formattedDate, safeCustomerName, formattedTime);
				String dest = directoryPath + "\\" + fileName; // Full path including filename

				// Ensure the directory exists
				File directory = new File(directoryPath);
				if (!directory.exists()) {
					if (!directory.mkdirs()) {
						JOptionPane.showMessageDialog(null, "Failed to create directory: " + directoryPath, "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				createPdf(dest);
				openPDF(dest); // Open the PDF after creation

			}
			private void calculateAndDisplayPayableAmount() {
			    int deliveryCharge = 0; // Initialize delivery charge to 0
			    String deliveryText = textField_10.getText().trim(); // Get delivery charge from textField_10 and remove leading/trailing whitespace
			    if (!deliveryText.isEmpty()) { // Check if the input is not empty
			    	
			    	try {
			            deliveryCharge = Integer.parseInt(deliveryText); // Parse the delivery charge
			        } catch (NumberFormatException ex) {
			            // Handle invalid input
			            JOptionPane.showMessageDialog(null, "Invalid delivery charge format.", "Error", JOptionPane.ERROR_MESSAGE);
			            ex.printStackTrace();
			            return; // Exit the method if parsing fails
			        }
			    }

			    // Check if delivery charge is 0 or empty
			    if (deliveryCharge == 0 && deliveryText.isEmpty()) {
			        payableAmount = totalAmount - offerPrice; // Calculate payable amount without including delivery charge
			    } else {
			        payableAmount = totalAmount - offerPrice + deliveryCharge; // Include delivery charge in the payable amount
			    }

			    textField_5.setText("Payable Amount: " + String.format("%.2f\n", payableAmount)); // Update payable amount display
			}



			private void calculateAndDisplayOfferPrice() {

				String discountText = textField_8.getText().trim(); // Get the text from the discount field
				if (discountText.isEmpty()) {
					offerPrice = 0;
				} else {
					try {
						double discountPercent = Double.parseDouble(discountText);
						offerPrice = totalAmount * (discountPercent / 100);
					} catch (NumberFormatException ex) {
						textField_4.setText("Offer Price: Invalid discount format.\n");
						return;
					}
				}
				textField_4.setText("Discount: " + String.format("%.2f\n", offerPrice)); // Clear previous text
			}

			private void updateTotal() {
				totalAmount = 0.0; // Reset total
				for (int i = 0; i < model.getRowCount(); i++) {
					Object value = model.getValueAt(i, 4); // Assume total is in the 5th column
					if (value instanceof Number) {
						totalAmount += ((Number) value).doubleValue();
					}
				}
				textField_9.setText("Total: " + String.format("%.2f\n", totalAmount));

			}

			private void createPdf(String dest) {
				 Document document = new Document();

				AtomicReference<Image> imgRef = new AtomicReference<>(); // Use AtomicReference to hold the image

				try {
					PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));

					try {
						imgRef.set(Image.getInstance("C:\\pravdaBooksBillManagement\\pdfimg.jpg")); // Load and store
																									// the image

						imgRef.get().setAbsolutePosition(0, 0); // Position the image
						imgRef.get().scaleToFit(document.getPageSize()); // Scale the image to fit the page size
					} catch (IOException e) {
						System.err.println("Unable to load background image: " + e.getMessage());
					}

					// Register a page event to add the image as a background
					writer.setPageEvent(new PdfPageEventHelper() {
						@Override
						public void onEndPage(PdfWriter writer, Document document) {
							PdfContentByte canvas = writer.getDirectContentUnder();
							try {
								if (imgRef.get() != null) {
									canvas.addImage(imgRef.get());
								}
							} catch (DocumentException e) {
								System.err.println("Error adding image to PDF: " + e.getMessage());
							}
						}
					});

					document.open();

					// Date and Time setup
					LocalDate currentDate = LocalDate.now();
					LocalTime currentTime = LocalTime.now();
					DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
					DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");
					String dateTime = "\n\n\n\n\n\n\n\n\nDate: " + currentDate.format(dateFormatter) + "\nTime: "
							+ currentTime.format(timeFormatter);

					// Table for date and time at the right top corner
					PdfPTable dateTimeTable = new PdfPTable(1); // 1 column
					dateTimeTable.setWidthPercentage(100); // Width 100%
					PdfPCell dateTimeCell = new PdfPCell(
							new Phrase(dateTime, FontFactory.getFont(FontFactory.HELVETICA, 12)));
					dateTimeCell.setBorder(Rectangle.NO_BORDER);
					dateTimeCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					dateTimeTable.addCell(dateTimeCell);
					document.add(dateTimeTable);

					// Increment the last used bill number to get the next bill number
					int nextBillNumber = BillNumberManager.getNextBillNumber();

					// Generate the bill number using the next bill number
					String billNO = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMYY"))  +
						"-" +nextBillNumber;

					// Adding bill number to the document
					Paragraph billNumberParagraph = new Paragraph();
					billNumberParagraph.add(new Chunk("\nBill Number: ", FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD)));
					billNumberParagraph.add(new Chunk(billNO, FontFactory.getFont(FontFactory.HELVETICA, 12)));
					billNumberParagraph.setAlignment(Element.ALIGN_LEFT);
					document.add(billNumberParagraph);

					// Customer Name
					String customerName = textField_2.getText(); // Assuming textField_2 is accessible here
					Paragraph customerNameParagraph = new Paragraph();
					customerNameParagraph.add(new Chunk("\nName and Address: ",
							FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD)));
					customerNameParagraph.add(new Chunk(customerName, FontFactory.getFont(FontFactory.HELVETICA, 12)));

					customerNameParagraph.setAlignment(Element.ALIGN_LEFT);
					document.add(customerNameParagraph);

					// Heading
					document.add(new Paragraph("\n\nPurchasing Details",
							FontFactory.getFont(FontFactory.HELVETICA, 13, Font.PLAIN)));

					// Table for itemized billing
					PdfPTable table = new PdfPTable(new float[] { 1, 3, 2, 2, 2 }); // Create a table with 5 columns
					table.setWidthPercentage(100); // Set table width to 100%
					table.setSpacingBefore(20);
					table.setSpacingAfter(20);
					String[] headers = { "No", "Book Name", "Rate", "Quantity", "Total" };
					for (String header : headers) {
						PdfPCell cell = new PdfPCell(new Paragraph(header));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(cell);
					}
					for (int i = 0; i < model.getRowCount(); i++) {
						for (int j = 0; j < model.getColumnCount(); j++) {
							PdfPCell cell = new PdfPCell(new Paragraph(model.getValueAt(i, j).toString()));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(cell);
						}
					}
					document.add(table);

					// Additional billing details
					document.add(new Paragraph("Total: " + String.format("%.2f", totalAmount),
							FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
					document.add(new Paragraph("Discount: " + String.format("%.2f", offerPrice),
							FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
					if (deliveryText.isEmpty()) {
					    document.add(new Paragraph("Delivery Charge: 0",
					            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
					} else {
					    try {
					        double deliveryChargeValue = Double.parseDouble(deliveryText);
					        document.add(new Paragraph("Delivery Charge: " + String.format("%.2f", deliveryChargeValue),
					                FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
					    } catch (NumberFormatException ex) {
					        document.add(new Paragraph("Delivery Charge: Invalid",
					                FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
					    }
					}

					document.add(new Paragraph("Payable Amount: " + String.format("%.2f", payableAmount),
							FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
					

					document.close();
					System.out.println("PDF Created!");
				} catch (DocumentException | FileNotFoundException ex) {
					JOptionPane.showMessageDialog(null, "Failed to create PDF: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}

			private void openPDF(String filePath) {
				if (Desktop.isDesktopSupported()) {
					try {
						File myFile = new File(filePath);
						Desktop.getDesktop().open(myFile);
						clearTableAndFields();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}

			private void clearTableAndFields() {
				model.setRowCount(0); // Clear the table
				textField_2.setText(""); // Clear customer name field
				textField_8.setText(""); // Clear discount field
				textField_9.setText(""); // Clear total field
				textField_4.setText(""); // Clear discount field
				textField_5.setText(""); // Clear payable amount field
				textField_10.setText("");
			}

		});
		contentPane.add(btnNewButton_3);
		
		JLabel lblNewLabel_8 = new JLabel("Delivery Charge");
		lblNewLabel_8.setForeground(new Color(0, 0, 128));
		lblNewLabel_8.setFont(new Font("Dialog", Font.BOLD, 12));
		lblNewLabel_8.setBounds(318, 523, 121, 19);
		contentPane.add(lblNewLabel_8);
		
		

	}
}
