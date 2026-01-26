# ❄️ SnowySMP Sell GUI ❄️

A lightweight, robust, and **dupe-proof** Minecraft Spigot plugin built for **1.21.1**. This plugin provides an interactive economy system for the **SnowySMP** server, allowing players to sell items via GUI and admins to manage market prices in real-time.

## 🛠 Features
* **Interactive Sell GUI (`/sellgui`):** A 54-slot inventory for selling items. 
* **Dupe-Proof Security:** Items are processed safely upon closing the menu.
* **Price Lookup:** Check individual item values and full stack values while holding an item.
* **Visual Price List:** A clean GUI (`/worth list`) showing every item available for sale and its price.
* **Admin Management:** Remove items from the market or restore them via `/worth remove list`.
* **Vault Integration:** Seamless connection to your server's economy.

## 📋 Commands
| Command | Permission | Description |
| :--- | :--- | :--- |
| `/sellgui` | `guisell.use` | Opens the Sell GUI. |
| `/worth` | `guisell.use` | Displays the value of the item in your hand. |
| `/worth list` | `guisell.use` | Opens a GUI showing all prices. |
| `/worth remove [item]` | `guisell.admin` | Removes an item's price. |
| `/worth remove list` | `guisell.admin` | Opens the **SnowySMP Removed Items** GUI. |

## ⚙️ Dependencies
* **Spigot/Paper 1.21.1**
* **Vault** (Required for Economy)
* **Java 21**

---
** *If you need a custom plugin or complex systems, feel free to reach out!* **Discord:** zainim0