const Footer = () => {
    return (
        <footer className="bg-white p-6 xl:flex gap-6">
            <div className="text-[#FAFAFA] space-y-3 bg-[#00371E] rounded py-12 pl-10 pr-[72px] flex-shrink-0">
                <h4 className="font-bold text-3xl md:text-4xl lg:text-5xl">
                    About <span className="text-[#08CF74]">NACOS</span>
                </h4>
                <p className=" tracking-[2%] text-sm lg:text-base lg:max-w-[80%] xl:max-w-[385px]">
                    NACOS stands for Nigeria Association of Computing Students(NACOS) formerly known
                    as Nigeria Association of Computer Science Students(NACOSS) Students. <br />
                    <br /> This is expressed in her slogan "Towards Advanced Computing". The
                    standards have been followed and improved upon from teams to teams in different
                    institutions and collectively.
                </p>
            </div>

            <div className="flex flex-col space-y-6 w-full">
                <div className="w-full flex justify-between pt-9 pb-4 md:p-9">
                    <div className="flex gap-12">
                        {[
                            {
                                header: "Quick Links",
                                contents: [
                                    { name: "Home", url: "/" },
                                    { name: "Make Payment", url: "#" },
                                    { name: "About Us", url: "#" },
                                    { name: "Contact", url: "#" },
                                ],
                            },
                            {
                                header: "Our Socials",
                                contents: [
                                    {
                                        name: "X (Formerly Twitter)",
                                        url: "https://twitter.com/NacosFunaab",
                                    },
                                    {
                                        name: "Instagram",
                                        url: "https://www.instagram.com/nacos_funaab/",
                                    },
                                ],
                            },
                        ].map((col) => (
                            <ul key={col.header} className="space-y-6 font-medium text-sm">
                                <li className="text-[#8D8484]">{col.header}</li>

                                <div className="space-y-4">
                                    {col.contents.map((content) => (
                                        <li key={content.name} className="text-[#3E3838]">
                                            <a
                                                href={content.url}
                                                className="hover:underline hover:underline-offset-4"
                                            >
                                                {content.name}
                                            </a>
                                        </li>
                                    ))}
                                </div>
                            </ul>
                        ))}
                    </div>

                    <div className="hidden md:flex flex-col items-end justify-between">
                        <div className="flex gap-3 items-center">
                            <img src="/logo.svg" width={48} height={48} alt="" />
                            <p className="font-bold text-lg lg:text-xl text-[#00371E]">
                                NACOS,
                                <br /> FUNAAB
                            </p>
                        </div>

                        <p className="text-right text-[#514A4A] font-semibold text-sm lg:text-base">
                            Copyright &copy; 2024. All Rights Reserved
                        </p>
                    </div>
                </div>

                <div className="flex flex-col md:flex-row md:items-center gap-8 md:gap-16 md:p-4">
                    {[
                        {
                            name: "Temiloluwa Akinbote",
                            role: "designer",
                            displayPic: "images/temi.png",
                            icon: "/pen.svg",
                            redirectUrl: "https://x.com/Temilo1Akinbote?t=8Z__1ci26LJJi_Mvnq5Qlg",
                        },
                        {
                            name: "David Enikuomehin",
                            role: "developer",
                            displayPic: "https://github.com/eniiku.png",
                            icon: "/code.svg",
                            redirectUrl: "https://github.com/eniiku",
                        },
                    ].map((credit) => (
                        <div key={credit.name} className="flex gap-4">
                            <div className="relative flex-shrink-0">
                                <img
                                    src={credit.displayPic}
                                    width={85}
                                    height={85}
                                    alt=""
                                    className="rounded-xl bg-slate-300"
                                />

                                <div className="absolute bottom-0 -right-2.5 w-8 h-8 rounded-full border-2 border-white bg-[#FFDD00] grid place-items-center">
                                    <img src={credit.icon} alt="" />
                                </div>
                            </div>

                            <div className="space-y-2">
                                <div>
                                    <p className="text-[#313131]/80 text-sm md:text-base">
                                        {credit.role === "designer"
                                            ? "Designed by"
                                            : "Developed by"}
                                    </p>
                                    <h6 className="text-nowrap text-base md:text-[18px] font-bold text-[#313131] leading-[26px] tracking-[3%]">
                                        {credit.name}
                                    </h6>
                                </div>

                                <a
                                    href={credit.redirectUrl}
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    className="block w-fit rounded-full py-2 px-3 bg-custom-green/20 text-custom-green font-semibold text-xs md:text-sm text-nowrap transition-all duration-300 hover:bg-custom-green/80 hover:text-white/80 hover:shadow-lg"
                                >
                                    See his works
                                </a>
                            </div>
                        </div>
                    ))}
                </div>

                <p className="md:hidden text-center text-[#514A4A] font-semibold text-xs">
                    Copyright &copy; 2024. All Rights Reserved
                </p>
            </div>
        </footer>
    )
}

export default Footer
