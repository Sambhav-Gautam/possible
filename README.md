# 🛤️ Journey Stops Tracker  

This is a simple and efficient RecyclerView adapter (`StopsAdapter`) for displaying a list of journey stops. It highlights the current stop, shows whether a visa is required, and provides distance information—all in a clean and minimal UI.  

---

## ✨ What This Does  

- Displays a **list of travel stops** in a RecyclerView.  
- **Highlights the current stop** so users know where they are.  
- Shows if a **visa is required** for each stop.  
- Displays the **distance** to each stop.  
- Uses **lazy loading**, meaning it only loads what’s visible on the screen, keeping things smooth and fast.  

---

## 🎨 How It Looks  

- Each stop is displayed as a card with the name, visa requirement, and distance.  
- The **current stop is highlighted** with a light orange background (`#FFDDC1`).  
- Stops update dynamically when the user moves forward in their journey.  

---

## 🚀 How to Use  

### 1️⃣ Setting Up RecyclerView  
Add the adapter to your RecyclerView in your Activity or Fragment:  

```kotlin
val stopsList = listOf(
    Stop("New York", true, 150.0),
    Stop("London", false, 300.0),
    Stop("Paris", true, 220.0)
)

val adapter = StopsAdapter(stopsList, 0) // Starting at the first stop
recyclerView.adapter = adapter
```

### 2️⃣ Updating the Current Stop  
As the journey progresses, update the highlighted stop:  

```kotlin
adapter.updateCurrentStopIndex(newIndex)
```

This ensures the old stop resets and the new one is highlighted.  

---

## 🔍 How It Works  

### 🏗️ The Adapter (`StopsAdapter.kt`)  
This class takes a list of stops and the current stop index. It dynamically updates the UI when the journey progresses.  

### 📜 Data Model  
```kotlin
data class Stop(val name: String, val visaRequired: Boolean, val distance: Double)
```

### 🖼️ XML Layout (`stop_item.xml`)  
Make sure your item layout has these IDs:  

```xml
<LinearLayout android:id="@+id/stopContainer">
    <TextView android:id="@+id/stopText" />
    <TextView android:id="@+id/visaRequirementText" />
    <TextView android:id="@+id/distanceText" />
</LinearLayout>
```

### ⚡ Lazy Loading  
- **Why it’s fast?** RecyclerView **only loads visible items**, saving memory.  
- **When scrolling,** new items are loaded dynamically.  
- **Only updates necessary items,** making it super efficient.  

---

## 🎯 What You’ll See  

✅ A clean list of journey stops.  
✅ The current stop **highlights in orange**.  
✅ Smooth updates as the journey progresses.  

