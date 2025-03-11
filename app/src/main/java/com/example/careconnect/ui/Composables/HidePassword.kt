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

public val HidePassword: ImageVector
	get() {
		if (_HidePassword != null) {
			return _HidePassword!!
		}
		_HidePassword = ImageVector.Builder(
            name = "HidePassword",
            defaultWidth = 800.dp,
            defaultHeight = 800.dp,
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
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(2f, 2f)
				lineTo(22f, 22f)
			}
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF000000)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 2f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(6.71277f, 6.7226f)
				curveTo(3.66480f, 8.79530f, 20f, 120f, 20f, 120f)
				curveTo(20f, 120f, 5.63640f, 190f, 120f, 190f)
				curveTo(14.05030f, 190f, 15.81740f, 18.27340f, 17.27110f, 17.28840f)
				moveTo(11f, 5.05822f)
				curveTo(11.32540f, 5.02010f, 11.65880f, 50f, 120f, 50f)
				curveTo(18.36360f, 50f, 220f, 120f, 220f, 120f)
				curveTo(220f, 120f, 21.30820f, 13.33170f, 200f, 14.83350f)
			}
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF000000)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 2f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(14f, 14.2362f)
				curveTo(13.46920f, 14.71120f, 12.76840f, 15.00010f, 120f, 15.00010f)
				curveTo(10.34310f, 15.00010f, 90f, 13.6570f, 90f, 12.00010f)
				curveTo(90f, 11.17640f, 9.33190f, 10.43030f, 9.86930f, 9.88820f)
			}
		}.build()
		return _HidePassword!!
	}

private var _HidePassword: ImageVector? = null
