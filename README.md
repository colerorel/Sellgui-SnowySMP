# ❄️ SnowySMP Sell GUI ❄️

A lightweight, robust, and **dupe-proof** Minecraft Spigot plugin built for **1.21.1**. This plugin provides an interactive economy system for the **SnowySMP** server, allowing players to sell items via GUI and admins to manage market prices in real-time.

## 🛠 Features
* **Interactive Sell GUI (`/sellgui`):** A 54-slot inventory for selling items. 
* **Dupe-Proof Security:** Items are never lost. If a player closes the GUI without selling or if the server restarts, items are safely returned to the player's inventory or dropped at their feet.
* **Price Lookup:** Check individual item values and full stack values while holding an item.
* **Visual Price List:** A clean GUI (`/worth list`) showing every item available for sale and its price.
* **Admin Restore System:** A dedicated GUI to view removed items and restore them to the market individually or use the "Restore All" function.
* **Vault Integration:** High-performance connection to your server's economy.
* **Required Plugins** Vault, EssentialsX, EconomyShopGUI

## 📋 Commands
| Command | Permission | Description |
| :--- | :--- | :--- |
| `/sellgui` | Player | Opens the Sell GUI. Items are sold upon clicking the Star or closing the menu. |
| `/worth` | Player | Displays the value of the item in your hand (and stack value). |
| `/worth list` | Player | Opens a GUI showing all items currently for sale. |
| `/worth remove` | Admin | Removes the item in your hand from the market instantly. |
| `/worth remove list` | Admin | Opens the **Restore GUI** to bring back removed items or "Restore All." |

## ⚙️ Configuration
The plugin generates a `worth.yml` file. It automatically tracks both active prices and items you have removed.

```yaml
prices:
  DIAMOND: 100.0
  IRON_INGOT: 10.0
removed:
  GRASS_BLOCK: 5.0
