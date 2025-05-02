package org.tonstudio.tact.notifications

import com.intellij.notification.NotificationType

class TactWarningNotification(content: String = "") :
    TactNotification(content, NotificationType.WARNING)
