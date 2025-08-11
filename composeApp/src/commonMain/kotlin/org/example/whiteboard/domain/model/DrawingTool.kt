package org.example.whiteboard.domain.model

import org.jetbrains.compose.resources.DrawableResource
import whiteboard.composeapp.generated.resources.Res
import whiteboard.composeapp.generated.resources.ic_arrow_one_sided
import whiteboard.composeapp.generated.resources.ic_circle_outline
import whiteboard.composeapp.generated.resources.ic_line_plain
import whiteboard.composeapp.generated.resources.ic_rectangle_outline
import whiteboard.composeapp.generated.resources.ic_triangle_outline
import whiteboard.composeapp.generated.resources.img_eraser
import whiteboard.composeapp.generated.resources.img_highlighter
import whiteboard.composeapp.generated.resources.img_laser_pen
import whiteboard.composeapp.generated.resources.img_pen

enum class DrawingTool(
    val iconRes: DrawableResource,
    val isColored: Boolean = false
) {

    PEN(iconRes = Res.drawable.img_pen, isColored = true),
    HIGH_LIGHTER(iconRes = Res.drawable.img_highlighter, isColored = true),
    ERASER(iconRes = Res.drawable.img_eraser, isColored = true),
    LASER_PEN(iconRes = Res.drawable.img_laser_pen, isColored = true),
    LINE(iconRes = Res.drawable.ic_line_plain),
    ARROW(iconRes = Res.drawable.ic_arrow_one_sided),
    RECTANGLE(iconRes = Res.drawable.ic_rectangle_outline),
    CIRCLE(iconRes = Res.drawable.ic_circle_outline),
    TRIANGLE(iconRes = Res.drawable.ic_triangle_outline),

}