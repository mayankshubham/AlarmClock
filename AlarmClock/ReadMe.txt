AlarmPage.java

Turn on button: 			extra sring in intent:from the button

1. Set the alarm manager
	intent(tell receiver to send the signal)
	pending intent(tells the alarm manger to send a delayed intent)
	calendar time(when the alarm manager broadcasts to receiver)

Turn off alarm button
1. Cancel the pending intent from the alarm manager (unsets alarm)
	note: this does not stop any ringtones

2. Stop the ringtone


AlarmReceiver.java

	starts the ringtone service at the time the alarm is supposed to go off

RingtoneService.java

	be careful to only use on instance of the mediaplayer
	1. take into account any extra strings from intent(this will tell the ringtone service whether the broadcase signal
	   is fron the on-button of the off-button)
	2. various if-else statements that will either start of stop the ringtone


Note:

In order to send the broadcasts we need to give our application the permission to we need to include that in the manifest file
like

<receiver android:name = ".AlarmReciever"/>

We also need to specify to play our ringtone so specify a service liek

<service android:name = ".RingtonePlayingService" android:enabled="true"/>


OK -> pending intent
when we want to set the alarm, we want to hear a ringtone at a later delayed time

CANCEL -> send Broadcast
when we are woken up by the ringtone, we want to stop it

	-> cancel pending intent
	cancelling the pending intent will unset the alarm, but it will not stop a ringtone
	it will stop the ringtone service


Extra string from intent : alarm on    service start id: 1
Extra string fron intent: alarm off    service start id: 0




