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

public val Notify: ImageVector
	get() {
		if (_Notify != null) {
			return _Notify!!
		}
		_Notify = ImageVector.Builder(
            name = "Notify",
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
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(7.556f, 8.5f)
				horizontalLineToRelative(8f)
				moveToRelative(-8f, 3.5f)
				horizontalLineTo(12f)
				moveToRelative(7.111f, -7f)
				horizontalLineTo(4.89f)
				arcToRelative(0.896f, 0.896f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.629f, 0.256f)
				arcToRelative(0.868f, 0.868f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.26f, 0.619f)
				verticalLineToRelative(9.25f)
				curveToRelative(00f, 0.2320f, 0.0940f, 0.4550f, 0.260f, 0.6190f)
				arcTo(0.896f, 0.896f, 0f, isMoreThanHalf = false, isPositiveArc = false, 4.89f, 16f)
				horizontalLineTo(9f)
				lineToRelative(3f, 4f)
				lineToRelative(3f, -4f)
				horizontalLineToRelative(4.111f)
				arcToRelative(0.896f, 0.896f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.629f, -0.256f)
				arcToRelative(0.868f, 0.868f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.26f, -0.619f)
				verticalLineToRelative(-9.25f)
				arcToRelative(0.868f, 0.868f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.26f, -0.619f)
				arcToRelative(0.896f, 0.896f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.63f, -0.256f)
				close()
			}
		}.build()
		return _Notify!!
	}

private var _Notify: ImageVector? = null
