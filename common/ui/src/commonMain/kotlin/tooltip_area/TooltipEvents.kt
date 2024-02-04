package tooltip_area

import BaseEvent
import main_models.TooltipItem

sealed class TooltipEvents: BaseEvent {
   open class SetTooltipEvent(val tooltip: TooltipItem): TooltipEvents()
}