package org.tonstudio.tact.notifications

import com.intellij.notification.NotificationType

class TactErrorNotification(content: String = "") :
    TactNotification(content, NotificationType.ERROR)
