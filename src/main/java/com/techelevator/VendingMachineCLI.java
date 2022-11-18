package com.techelevator;

import com.techelevator.model.Item;
import com.techelevator.utilities.Logger;
import com.techelevator.utilities.ObjectLoader;
import com.techelevator.view.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_EXIT = "Exit";
	private static final String MAIN_MENU_SALES_REPORT_SECRET = "";
	private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_EXIT, MAIN_MENU_SALES_REPORT_SECRET};

	private static final String PURCHASE_MENU_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_FINISH_TRANSACTION = "Finish Transaction";
	private static final String PURCHASE_MENU_DOES_NOTHING = null;
	private static final String[] PURCHASE_MENU_OPTIONS = {PURCHASE_MENU_FEED_MONEY, PURCHASE_MENU_SELECT_PRODUCT, PURCHASE_MENU_FINISH_TRANSACTION, PURCHASE_MENU_DOES_NOTHING };

	private static final String topBanner = "\n*********************************************";
	private static final String bottomBanner = "*********************************************\n";

	private final int DEFAULT_MAX_ITEMS = 5;
	private final int QUARTER_VALUE = 25;
	private final int DIME_VALUE = 10;
	private final int NICKEL_VALUE = 5;

	private BigDecimal balance = new BigDecimal(0.00);
	private Logger vendingMachineLog = new Logger("Log");
	private Map<String, Item> selectionsAvailable = new TreeMap<>();
	private Map<String, List<Item>> currentInventory = new TreeMap<>();

	private String itemToPurchaseSlotID;
	private Logger salesReport;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd   HH-mm-ss");

	private Menu menu;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}


	// Main method
	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.stockVendingMachine();
		cli.run();
	}

	private void stockVendingMachine() {
		File inventoryFile = new File("vendingmachine.csv");
		selectionsAvailable = ObjectLoader.generateInventoryItems(inventoryFile);

		for (Item item : selectionsAvailable.values()) {
			List<Item> itemInventory = new ArrayList<>();
			for (int i = 0; i < DEFAULT_MAX_ITEMS; i++) {
				itemInventory.add(item);
			}

			currentInventory.put(item.getSlotID().toLowerCase(), itemInventory);
		}
	}


	private void run() {
		System.out.println("WELCOME THE VENDING MACHINE OF THE FUTURE!!!");
		mainMenu();
	}

	private void mainMenu() {
		boolean runMainMenu = true;

		while (runMainMenu) {
			String userSelection = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (userSelection == null) {
				System.out.println("Sorry, you must enter a valid selection");
			} else {

				switch (userSelection) {
					case (MAIN_MENU_OPTION_DISPLAY_ITEMS):
						printCurrentStock();
						break;
					case (MAIN_MENU_OPTION_PURCHASE):
						purchaseMenu();
						break;
					case (MAIN_MENU_EXIT):
						System.exit(0);
					case (MAIN_MENU_SALES_REPORT_SECRET):
						generateSalesReport();
						System.out.println("Sales Report created");
						break;
					default:
						System.out.println("\nSorry, you did not enter 1, 2, or 3\n");
						break;
				}
			}
		}
	}


	private void printCurrentStock() {
		System.out.println("CURRENT STOCK IS: ");
		for (String slotID : currentInventory.keySet()) {
			if (currentInventory.get(slotID).size() > 0) {
				System.out.println(selectionsAvailable.get(slotID) + ", amount left:" + currentInventory.get(slotID).size());
			} else if (currentInventory.get(slotID).size() == 0) {
				System.out.println(selectionsAvailable.get(slotID) + ", SOLD OUT");
			}
		}
	}

	private void purchaseMenu() {
		boolean runPurchaseMenu = true;

		while (runPurchaseMenu) {
			System.out.println("\n--------------------------");
			System.out.println("| Current balance: $" + balance.setScale(2) + " |");
			System.out.println("--------------------------");

			String userSelection = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);

			switch (userSelection) {
				case (PURCHASE_MENU_FEED_MONEY):
					feedMoney();
					break;
				case (PURCHASE_MENU_SELECT_PRODUCT):

					BigDecimal lowestPrice = new BigDecimal(0);

					// Grab a starting value
					for (Item item : selectionsAvailable.values()) {
						lowestPrice = item.getPrice();
					}

					// Loop over the item map to find the lowest price.
					for (Item item : selectionsAvailable.values()) {
						if (item.getPrice().compareTo(lowestPrice) == -1) {
							lowestPrice = item.getPrice();
						}
					}

					while (balance.compareTo(lowestPrice) != -1) {
						selectProducts();
					}
//                    selectProducts();
					System.out.println("\n*** Must add funds or finish transaction ***");
					break;
				case (PURCHASE_MENU_FINISH_TRANSACTION):
					finishTransaction();
					run();
					break;
				default:
					System.out.println("\nSorry, you did not enter 1, 2, or 3\n");
					break;
			}
		}

	}

	private void feedMoney() {
		boolean runFeedMoney = true;

		while (runFeedMoney) {
			System.out.print("\n" + bottomBanner + "Current balance: $" + balance.setScale(2, RoundingMode.HALF_UP) +
					"\nHow much money would you like to add? (Enter a number or (R) to return: ");

			Double userNumber = 0.00;
			String userEntry = menu.generalInput();

			if (!userEntry.equalsIgnoreCase("R") && !userEntry.equals("")) {
				try {
					userNumber = Double.parseDouble(userEntry);
				} catch (NumberFormatException nfe) {
					System.out.println("\n### Sorry you did not enter a valid number ###\n");
				}
			}

			// ReLoop for any none whole numbers or null values
			if (userNumber % 1 != 0 && userNumber != 0.00 || userEntry.equals("")) {
				System.out.println("\n*** Please enter whole number or (R) ***\n");
				feedMoney();
				break;
			}

			if (userEntry.equalsIgnoreCase("R")) {
				runFeedMoney = false;
				break;
			} else {
				BigDecimal moneyAdded = new BigDecimal(userNumber);
				balance = balance.add(moneyAdded);
				vendingMachineLog.log("FEED MONEY: " + moneyAdded.setScale(2) + " " + balance.setScale(2));
			}
		}
	}

	private void selectProducts() {
		printCurrentStock();

		System.out.println("--------------------------");
		System.out.println("| Current balance: $" + balance.setScale(2) + " |");
		System.out.println("--------------------------\n");

		System.out.print("Enter the SlotID for the product you would like to purchase or press (R) to return: ");
		String slotID = menu.generalInput();

		if (slotID == null) {
			System.out.println("\n*** Sorry, you must enter a valid selection ***\n");
		} else {
			itemToPurchaseSlotID = slotID.toLowerCase();
			boolean isItemFound = currentInventory.containsKey(itemToPurchaseSlotID);

			if (itemToPurchaseSlotID.equals("r")) {
				System.out.println("Test spot");
				purchaseMenu();
			}

			if (isItemFound == false) {
				System.out.println("Sorry, you must enter a valid selection");
			} else if (currentInventory.get(itemToPurchaseSlotID).size() == 0) {
				System.out.println("Sorry, we are all sold out of " + selectionsAvailable.get(itemToPurchaseSlotID).getName());
			} else if (selectionsAvailable.get(itemToPurchaseSlotID).getPrice().compareTo(balance) == 1) {
				insufficientFunds();
			} else if (!(selectionsAvailable.get(itemToPurchaseSlotID).getPrice().compareTo(balance) == 1)) {
				dispenseItem();
			}
		}
	}

	private void finishTransaction() {
		int remainingBalance = (balance.multiply(new BigDecimal(100.00))).intValue();

		int numberOfQuarters = remainingBalance / QUARTER_VALUE;
		remainingBalance = remainingBalance - (numberOfQuarters * QUARTER_VALUE);

		int numberOfDimes = remainingBalance / DIME_VALUE;
		remainingBalance = remainingBalance - (numberOfDimes * DIME_VALUE);

		int numberOfNickels = remainingBalance / NICKEL_VALUE;
		remainingBalance = remainingBalance - (numberOfNickels * NICKEL_VALUE);

		balance = BigDecimal.valueOf(((double) remainingBalance) / 100.00);

		BigDecimal changeDispensed = BigDecimal.valueOf((double)
				((numberOfQuarters * QUARTER_VALUE) + (numberOfDimes * DIME_VALUE) + (numberOfNickels * NICKEL_VALUE)) / 100.00);

		System.out.println("\n" + topBanner + "$$$ DISPENSING $" + changeDispensed.setScale(2) + " in CHANGE $$$" + bottomBanner + "\n");
		System.out.println("Dispensing " + numberOfQuarters + " quarters, " + numberOfDimes + " dimes, and " + numberOfNickels + " nickels");

		vendingMachineLog.log("GIVE CHANGE: " + changeDispensed.setScale(2) + " " + balance);
	}


	// This function handles insufficient funds situations.
	private void insufficientFunds() {
		System.out.print("Sorry, you don't have enough funds.  Would you like to add money? (Enter (y)es or (n)o: ");
		String addfunds = menu.generalInput();
		if (addfunds == null) {
			System.out.println("Sorry, invalid entry...going to the Purchase Menu...");
		} else {
			if (!addfunds.equalsIgnoreCase("y") && !addfunds.equalsIgnoreCase("yes") &&
					!addfunds.equalsIgnoreCase("n") && !addfunds.equalsIgnoreCase("no")) {
				System.out.println("Sorry, invalid entry...going to the Purchase Menu...");
			} else if (addfunds.equalsIgnoreCase("y") || addfunds.equalsIgnoreCase("yes")) {
				feedMoney();
			} else if (addfunds.equalsIgnoreCase("n") || addfunds.equalsIgnoreCase("no")) {
				System.out.println("Returning to Purchase Menu...");
			}
		}
	}


	private void dispenseItem() {
		System.out.println("\n" + topBanner + "!!! DISPENSING ITEM !!!" + bottomBanner + "\n");
		Item dispensedItem = currentInventory.get(itemToPurchaseSlotID).remove(0);
		balance = balance.subtract(dispensedItem.getPrice());
		System.out.println(dispensedItem.dispensingItemNameAndPrice() + ", new balance: $"
				+ balance.setScale(2) + dispensedItem.getMessage());

		vendingMachineLog.log(dispensedItem.getName() + " " + dispensedItem.getSlotID() + " "
				+ dispensedItem.getPrice().setScale(2) + " " + balance.setScale(2));
	}


	private void generateSalesReport() {
		String dateAndTime = LocalDateTime.now().format(formatter);
		salesReport = new Logger("SalesReport-" + dateAndTime);
		File sourceFile = null;

		try {
			sourceFile = getFileFromPath("Logs/Log.txt");
		} catch (FileNotFoundException fnf) {
			System.out.println("Sorry Log.txt was not found");
			System.exit(1);
		}

		BigDecimal totalSales = new BigDecimal(0.00);

		for (Item item : selectionsAvailable.values()) {
			int counter = 0;
			try (Scanner fileScanner = new Scanner(sourceFile);) {

				while (fileScanner.hasNextLine()) {
					String loggedEvent = fileScanner.nextLine();
					if (loggedEvent.contains(item.getSlotID())) {
						counter++;
						totalSales = totalSales.add(item.getPrice());
					}
				}
				salesReport.salesLog(item.getName() + "|" + counter);

			} catch (FileNotFoundException fnf2) {
				System.out.println("Sorry file not found: " + sourceFile.getAbsolutePath());
				System.exit(3);
			}
		}
		salesReport.salesLog("**TOTAL SALES ** $" + totalSales.setScale(2));
	}

	private File getFileFromPath(String sourcePath) throws FileNotFoundException {
		File inputFile = new File(sourcePath);
		if (inputFile.exists() == false) {
			System.out.println(sourcePath + " does not exist");
			System.exit(1);
		} else if (inputFile.isFile() == false) {
			System.out.println(sourcePath + " is not a file");
			System.exit(1);
		}
		return inputFile;
	}

}
