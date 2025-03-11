/*
* Converted using https://composables.com/svgtocompose
*/

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Filter: ImageVector
	get() {
		if (_Filter != null) {
			return _Filter!!
		}
		_Filter = ImageVector.Builder(
            name = "Filter",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF000000)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 2f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(18.796f, 4f)
				horizontalLineTo(5.204f)
				arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.753f, 1.659f)
				lineToRelative(5.302f, 6.058f)
				arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.247f, 0.659f)
				verticalLineToRelative(4.874f)
				arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.2f, 0.4f)
				lineToRelative(3f, 2.25f)
				arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.8f, -0.4f)
				verticalLineToRelative(-7.124f)
				arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.247f, -0.659f)
				lineToRelative(5.302f, -6.059f)
				curveToRelative(0.5660f, -0.6460f, 0.1060f, -1.6580f, -0.7530f, -1.6580f)
				close()
			}
		}.build()
		return _Filter!!
	}

private var _Filter: ImageVector? = null
