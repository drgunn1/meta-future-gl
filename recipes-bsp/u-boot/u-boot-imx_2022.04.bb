# Copyright (C) 2022 Future Electronics

require recipes-bsp/u-boot/u-boot.inc
require future-u-boot-imx-common_${PV}.inc

PROVIDES += "u-boot"

inherit uuu_bootloader_tag

SRC_URI += " \
	file://imx8mp-futuregl.dts;subdir=git/arch/arm/dts 		\
	file://imx8mp-futuregl-u-boot.dtsi;subdir=git/arch/arm/dts	\
	file://imx8mp_futuregl.h;subdir=git/include/configs 		\
	file://imx8mp_futuregl_defconfig;subdir=git/configs		\
	file://board/future/imx8mp-futuregl/ddr4_timing.c;subdir=git/board/future/imx8mp-futuregl/ 	\
	file://board/future/imx8mp-futuregl/imx8mp_futuregl.c;subdir=git/board/future/imx8mp-futuregl/ 	\
	file://board/future/imx8mp-futuregl/imximage-8mp-lpddr4.cfg;subdir=git/board/future/imx8mp-futuregl/ 	\
	file://board/future/imx8mp-futuregl/Kconfig;subdir=git/board/future/imx8mp-futuregl/ 	\
	file://board/future/imx8mp-futuregl/lpddr4_timing.c;subdir=git/board/future/imx8mp-futuregl/ 	\
	file://board/future/imx8mp-futuregl/lpddr4_timing_ndm.c;subdir=git/board/future/imx8mp-futuregl/ 	\
	file://board/future/imx8mp-futuregl/MAINTAINERS;subdir=git/board/future/imx8mp-futuregl/ 	\
	file://board/future/imx8mp-futuregl/Makefile;subdir=git/board/future/imx8mp-futuregl/ 	\
	file://board/future/imx8mp-futuregl/spl.c;subdir=git/board/future/imx8mp-futuregl/ 	\
"

UUU_BOOTLOADER            = ""
UUU_BOOTLOADER:mx6-nxp-bsp        = "${UBOOT_BINARY}"
UUU_BOOTLOADER:mx7-nxp-bsp        = "${UBOOT_BINARY}"
UUU_BOOTLOADER_TAGGED     = ""
UUU_BOOTLOADER_TAGGED:mx6-nxp-bsp = "u-boot-tagged.${UBOOT_SUFFIX}"
UUU_BOOTLOADER_TAGGED:mx7-nxp-bsp = "u-boot-tagged.${UBOOT_SUFFIX}"

do_deploy:append:mx8m-nxp-bsp() {
    # Deploy u-boot-nodtb.bin and fsl-imx8m*-XX.dtb for mkimage to generate boot binary
    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    install -d ${DEPLOYDIR}/${BOOT_TOOLS}
                    install -m 0777 ${B}/${config}/arch/arm/dts/${UBOOT_DTB_NAME}  ${DEPLOYDIR}/${BOOT_TOOLS}
                    install -m 0777 ${B}/${config}/u-boot-nodtb.bin  ${DEPLOYDIR}/${BOOT_TOOLS}/u-boot-nodtb.bin-${MACHINE}-${type}
                fi
            done
            unset  j
        done
        unset  i
    fi
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx6-generic-bsp|mx7-generic-bsp|mx8-generic-bsp)"
