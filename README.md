# BurpFlow

**BurpFlow allows web vulnerability testers to group related HTTP requests into user-defined, logical workflows representing application behavior, including multi-step actions or entire sessions, to improve efficiency and traceability.**

**Created by Natalia Wunder**

---

## Features

- Capture HTTP requests from Proxy, sequentially or manually
- Organize traffic into named flows
- Display requests & responses in sequence in the BurpFlow Tab

---

## How to Use

### Proxy Tab

In the **Proxy Tab**, right-click on a request to begin a flow. You can begin a flow in two ways:

1. **Begin Flow on Next Request**  
   Capture flows sequentially by navigating the application.  
   To stop this flow, right-click on any request and click **Stop Current Flow**

2. **Add Request to Flow**  
   Manually add requests by selecting them from the Proxy history

### BurpFlow Tab

Flows will be shown in the **BurpFlow Tab**. You can add, rename, delete, and refresh flows using the buttons in the sidebar.

To view a flow and its requests, click on a flow in the sidebar.

Once a flow is selected:

- Requests and pertinent information will show in the Request Grid in sequential order
- Clicking on a row displays the HTTP **Request** and **Response** in the built-in editors
- Right-clicking on a row allows you to send the request to **Intruder** or **Repeater**
- Right-clicking one or multiple rows allows you to **delete** requests from the flow

---

## Notes & Recommendations

- **Scope MUST be set** for sequential flows to capture
- **Only manually added flows will persist in projects**  
  To save sequential flows, manually add them to a flow before leaving a project
- **Do not clear or delete the Proxy History**  
  Proxy IDs only show for manually added requests. If you delete the history, IDs in BurpFlow will no longer match those in the Proxy tab. The Proxy ID setting may be toggled on or off in the Configuration Tab
- **Ensure a flow is selected and highlighted in blue** in the sidebar when selecting or right-clicking a request  
  The action may not occur if the flow is not highlighted

---

## Build Instructions

To build the `.jar` file:

1. Make sure you have **Gradle** installed
2. Open your terminal and run:

   ```bash
   ./gradlew jar
3. The resulting `.jar` will be in the `build/libs/` directory (named `BurpFlow.jar`)

4. Open **Burp Suite** → **Extensions** → **Add**, and load the `.jar` as a **Java Extension**

---

## Future Implementations

- Persistence for sequential flows  
- Full Proxy ID matching for all requests
- Sequential flow capture without set scope
- Option to toggle Proxy highlight behavior

---

## Help & Feedback

For feedback, feature requests or bug reports, please contact:

**natalia.wunder@guidepointsecurity.com**  
**[BurpFlow GitHub Repository](https://github.com/nataliawunder/BurpFlow)**

---

## Acknowledgments

A special thank you to **Aaron Levin** and the **GPSU Team** for all your guidance and support during my internship at **GuidePoint Security University**. This project wouldn’t have been possible without you!

Additionally, thank you to **Elle Stehli** and **Gaberiel Bearden** for your help in implementation ideas for the Proxy ID functionality and cross-functionality debugging, respectively.


