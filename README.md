# ❄️ SnowySMP Sell GUI ❄️

A lightweight, custom Minecraft Spigot plugin designed for the **SnowySMP** server. This plugin provides an interactive GUI for players to sell items and an easy way to check item market values.

## 🛠 Features
* **Interactive Sell GUI:** A 54-slot inventory where players can place items to sell them instantly.
* **Smart Processing:** Items are automatically sold if the player closes the inventory or clicks the confirmation star.
* **Price Lookup:** Check individual item prices or stack values via command.
* **Visual Price List:** A paginated, read-only GUI that displays all available server prices.
* **Vault Integration:** Seamlessly connects with the server's economy system.

## 📋 Commands
| Command | Permission | Description |
| :--- | :--- | :--- |
| `/guisell` | Player | Opens the Sell GUI menu. |
| `/worth` | Player | Shows the value of the item in your main hand. |
| `/worth <item>` | Player | Shows the value of a specific item. |
| `/worth list` | Player | Opens a GUI menu showing all item prices. |
| `/guisell reload` | `guisell.admin` | Reloads the `worth.yml` configuration file. |

## ⚙️ Configuration
The plugin uses a `worth.yml` file to manage prices. You can add any Minecraft Material to the list and assign it a decimal value.

```yaml
prices:
  DIAMOND: 100.0
  IRON_INGOT: 10.0
  GOLD_INGOT: 25.0

# If you need a custom plugin like this, or something more complex 
# that works with other plugins on your server, feel free to reach out!
# Discord: zainim0
