


# CalendarFill

add CalenarFill component in your layout xml
```xml
<br.com.mirespeiti.calendarfill.calendardiario.CalendarFillView
        android:id="@+id/fill_calendar"
        android:layout_width="match_parent"
        android:layout_height="350dp"
        android:paddingBottom="20dp"
        app:type_callback="both"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toTopOf="@id/fill_fake"
        app:layout_constraintStart_toStartOf="parent"/>
```
configure the initial setup of Calendar Fill, through the initSetupToolbarCalendar() method
```kotlin
bind.fillFake.initSetupToolbarCalendar(  
    initMonth = Calendar.getInstance(),  
  datesColors = viewModel.fakeDates(Calendar.getInstance()),  
  notReturn = { viewModel.getFakeDates(it) },  
  adapter = YourCustomAdapterFill(calendarFillContext = bind.fillFake, selectedDay = Date()),  
  datesColors = listOf<CalendarItem>()  
    colors = object : ColorFill() {
    // set your colors   
        override var selected: Int = android.R.color.holo_red_dark  
  override var default: Int = android.R.color.holo_green_dark  
  override var secondary: Int = android.R.color.holo_purple  
  }           
)
```
<p>you can create your own calendar through the abstract class CalendarFull BaseAdapter</p>
#### has a default adapter (it's very nice), you don't need to create your own.

```kotlin 
abstract class CalendarFillBaseAdapter(  
 calendarFillContext: CalendarFillView,  
 open var colors: ColorFill = object : ColorFill() {},  
 open var dateColors: Array<CalendarioItem> = emptyArray(),  
 var currentMonth: Calendar,  
 var dates: List<Date> = calendarFillContext.fillDaysOnVisible(currentMonth),  
  @LayoutRes layout: Int = R.layout.item_calendar_adapter,  
) : ArrayAdapter<Date>(calendarFillContext.context, layout, dates), CalendarFillInterface {

... ... ...

}
```
<br>
<p>change the borders to a pattern you like</p>
 <img height="100%" width="100%" src="https://firebasestorage.googleapis.com/v0/b/livro-android-1327.appspot.com/o/pattern.png?alt=media&token=0c7156f2-6e2d-48e7-ab6a-8e612a99d136" />

#### Screenshots
<span>
<div>
<h5>Without filling the days of the month (default calendarfill) - Custom layout - Full month (default calendarfill) </h5>
<img height="80%" width="28%" src="https://firebasestorage.googleapis.com/v0/b/livro-android-1327.appspot.com/o/empty%20month.png?alt=media&token=baa3e712-28f7-4c17-b70f-93f89b836101" />
 <img height="80%" width="28%" src="https://firebasestorage.googleapis.com/v0/b/livro-android-1327.appspot.com/o/funny.png?alt=media&token=12eb7c57-d2c1-4f77-a103-b2163d2e6e24" />
 <img height="80%" width="28%" src="https://firebasestorage.googleapis.com/v0/b/livro-android-1327.appspot.com/o/fake%20month.png?alt=media&token=9ae3cc18-16bf-41d7-9a60-e3fc2c11ffd0" />
