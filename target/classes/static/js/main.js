// CreatorHub Main JavaScript File
// Handles all interactive functionality, animations, and user interactions

// Global variables
let currentStep = 1;
let selectedPeriod = 'monthly';
let isFollowing = false;

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    initializeAnimations();
    initializeInteractions();
    initializeParticles();
    initializeScrollEffects();
    initializeFormValidation();
    
    // Page-specific initializations
    const currentPage = getCurrentPage();
    switch(currentPage) {
        case 'index':
            initializeHomePage();
            break;
        case 'creator-profile':
            initializeCreatorProfile();
            break;
        case 'subscription':
            initializeSubscriptionPage();
            break;
    }
});

// Get current page identifier
function getCurrentPage() {
    const path = window.location.pathname;
    if (path.includes('creator-profile')) return 'creator-profile';
    if (path.includes('subscription')) return 'subscription';
    return 'index';
}

// Initialize scroll-triggered animations
function initializeScrollEffects() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('active');
            }
        });
    }, observerOptions);
    
    // Observe all elements with reveal class
    document.querySelectorAll('.reveal').forEach(el => {
        observer.observe(el);
    });
}

// Initialize particle system for hero section
function initializeParticles() {
    const particlesContainer = document.getElementById('particles-container');
    if (!particlesContainer) return;
    
    // Create floating particles using p5.js
    new p5((p) => {
        let particles = [];
        
        p.setup = () => {
            const canvas = p.createCanvas(particlesContainer.offsetWidth, particlesContainer.offsetHeight);
            canvas.parent(particlesContainer);
            canvas.style('position', 'absolute');
            canvas.style('top', '0');
            canvas.style('left', '0');
            canvas.style('z-index', '1');
            
            // Create particles
            for (let i = 0; i < 50; i++) {
                particles.push({
                    x: p.random(p.width),
                    y: p.random(p.height),
                    size: p.random(2, 6),
                    speedX: p.random(-0.5, 0.5),
                    speedY: p.random(-0.5, 0.5),
                    opacity: p.random(0.3, 0.8)
                });
            }
        };
        
        p.draw = () => {
            p.clear();
            
            particles.forEach(particle => {
                // Update position
                particle.x += particle.speedX;
                particle.y += particle.speedY;
                
                // Wrap around edges
                if (particle.x < 0) particle.x = p.width;
                if (particle.x > p.width) particle.x = 0;
                if (particle.y < 0) particle.y = p.height;
                if (particle.y > p.height) particle.y = 0;
                
                // Draw particle
                p.fill(255, 107, 107, particle.opacity * 255);
                p.noStroke();
                p.ellipse(particle.x, particle.y, particle.size);
            });
        };
        
        p.windowResized = () => {
            p.resizeCanvas(particlesContainer.offsetWidth, particlesContainer.offsetHeight);
        };
    });
}

// Initialize general interactions
function initializeInteractions() {
    // Navigation hover effects
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('mouseenter', () => {
            anime({
                targets: link,
                scale: 1.05,
                duration: 200,
                easing: 'easeOutQuad'
            });
        });
        
        link.addEventListener('mouseleave', () => {
            anime({
                targets: link,
                scale: 1,
                duration: 200,
                easing: 'easeOutQuad'
            });
        });
    });
    
    // Button hover effects
    document.querySelectorAll('.subscribe-btn, .glass-card').forEach(btn => {
        btn.addEventListener('mouseenter', () => {
            if (!btn.disabled) {
                anime({
                    targets: btn,
                    scale: 1.02,
                    duration: 200,
                    easing: 'easeOutQuad'
                });
            }
        });
        
        btn.addEventListener('mouseleave', () => {
            if (!btn.disabled) {
                anime({
                    targets: btn,
                    scale: 1,
                    duration: 200,
                    easing: 'easeOutQuad'
                });
            }
        });
    });
}

// Initialize animations
function initializeAnimations() {
    // Animate stats counters
    document.querySelectorAll('.stats-counter').forEach(counter => {
        const target = counter.textContent;
        const isPercentage = target.includes('%');
        const numericValue = parseInt(target.replace(/[^\d]/g, ''));
        
        anime({
            targets: { value: 0 },
            value: numericValue,
            duration: 2000,
            delay: 500,
            easing: 'easeOutQuad',
            update: function(anim) {
                const currentValue = Math.round(anim.animatables[0].target.value);
                if (isPercentage) {
                    counter.textContent = currentValue + '%';
                } else if (target.includes('K')) {
                    counter.textContent = (currentValue / 1000).toFixed(1) + 'K';
                } else if (target.includes('M')) {
                    counter.textContent = (currentValue / 1000000).toFixed(1) + 'M';
                } else {
                    counter.textContent = currentValue + '+';
                }
            }
        });
    });
}

// Initialize home page specific functionality
function initializeHomePage() {
    // Category filtering
    const categoryButtons = document.querySelectorAll('.category-btn');
    const creatorCards = document.querySelectorAll('.creator-card');
    
    categoryButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            // Update active button
            categoryButtons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            
            const category = btn.dataset.category;
            
            // Filter cards
            creatorCards.forEach(card => {
                const cardCategory = card.dataset.category;
                if (category === 'all' || cardCategory === category) {
                    card.style.display = 'block';
                    anime({
                        targets: card,
                        opacity: [0, 1],
                        translateY: [30, 0],
                        duration: 400,
                        delay: Math.random() * 200,
                        easing: 'easeOutQuad'
                    });
                } else {
                    anime({
                        targets: card,
                        opacity: 0,
                        translateY: -30,
                        duration: 300,
                        complete: () => {
                            card.style.display = 'none';
                        }
                    });
                }
            });
        });
    });
    
    // Search functionality
    const searchInput = document.querySelector('.search-input');
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const searchTerm = e.target.value.toLowerCase();
            
            creatorCards.forEach(card => {
                const creatorName = card.querySelector('h3').textContent.toLowerCase();
                const creatorType = card.querySelector('p').textContent.toLowerCase();
                
                if (creatorName.includes(searchTerm) || creatorType.includes(searchTerm)) {
                    card.style.display = 'block';
                } else {
                    card.style.display = 'none';
                }
            });
        });
    }
    
    // Subscribe button interactions
    document.querySelectorAll('.subscribe-btn').forEach(btn => {
        if (btn.textContent.includes('Subscribe')) {
            btn.addEventListener('click', (e) => {
                e.preventDefault();
                // Simulate subscription action
                anime({
                    targets: btn,
                    scale: [1, 0.95, 1],
                    duration: 200,
                    complete: () => {
                        showNotification('Redirecting to subscription page...', 'success');
                        setTimeout(() => {
                            window.location.href = 'subscription.html';
                        }, 1000);
                    }
                });
            });
        }
    });
}

// Initialize creator profile page
function initializeCreatorProfile() {
    // Follow button functionality
    const followBtn = document.getElementById('follow-btn');
    if (followBtn) {
        followBtn.addEventListener('click', () => {
            isFollowing = !isFollowing;
            
            if (isFollowing) {
                followBtn.textContent = 'Following';
                followBtn.classList.add('following');
                showNotification('Now following Sophia Chen!', 'success');
            } else {
                followBtn.textContent = 'Follow';
                followBtn.classList.remove('following');
                showNotification('Unfollowed Sophia Chen', 'info');
            }
            
            anime({
                targets: followBtn,
                scale: [1, 1.1, 1],
                duration: 300,
                easing: 'easeOutQuad'
            });
        });
    }
    
    // Tip button functionality
    const tipBtn = document.getElementById('tip-btn');
    if (tipBtn) {
        tipBtn.addEventListener('click', () => {
            showTipModal();
        });
    }
    
    // Content item interactions
    document.querySelectorAll('.content-item').forEach(item => {
        item.addEventListener('click', () => {
            const contentId = item.dataset.content;
            showContentModal(contentId);
        });
    });
    
    // Modal functionality
    const modal = document.getElementById('imageModal');
    const closeModal = document.getElementById('closeModal');
    
    if (closeModal) {
        closeModal.addEventListener('click', () => {
            modal.classList.remove('active');
        });
    }
    
    if (modal) {
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                modal.classList.remove('active');
            }
        });
    }
}

// Initialize subscription page
function initializeSubscriptionPage() {
    // Subscription period selection
    document.querySelectorAll('.payment-card').forEach(card => {
        card.addEventListener('click', () => {
            document.querySelectorAll('.payment-card').forEach(c => c.classList.remove('selected'));
            card.classList.add('selected');
            selectedPeriod = card.dataset.period;
            
            anime({
                targets: card,
                scale: [1, 1.02, 1],
                duration: 200,
                easing: 'easeOutQuad'
            });
        });
    });
    
    // Step navigation
    const continueToPayment = document.getElementById('continueToPayment');
    const backToDetails = document.getElementById('backToDetails');
    const completePayment = document.getElementById('completePayment');
    
    if (continueToPayment) {
        continueToPayment.addEventListener('click', () => {
            goToStep(2);
        });
    }
    
    if (backToDetails) {
        backToDetails.addEventListener('click', () => {
            goToStep(1);
        });
    }
    
    if (completePayment) {
        completePayment.addEventListener('click', () => {
            processPayment();
        });
    }
    
    // Form validation
    initializeFormValidation();
}

// Step navigation for subscription flow
function goToStep(step) {
    // Hide all step contents
    document.querySelectorAll('[id^="step"][id$="Content"]').forEach(content => {
        content.style.display = 'none';
    });
    
    // Show current step content
    const currentStepContent = document.getElementById(`step${step}Content`);
    if (currentStepContent) {
        currentStepContent.style.display = 'block';
        
        // Animate step content
        anime({
            targets: currentStepContent,
            opacity: [0, 1],
            translateY: [30, 0],
            duration: 400,
            easing: 'easeOutQuad'
        });
    }
    
    // Update progress indicators
    updateProgressIndicators(step);
    currentStep = step;
}

// Update progress indicators
function updateProgressIndicators(step) {
    for (let i = 1; i <= 3; i++) {
        const stepEl = document.getElementById(`step${i}`);
        const progressFill = document.getElementById('progressFill');
        
        if (stepEl) {
            stepEl.classList.remove('active', 'completed');
            
            if (i < step) {
                stepEl.classList.add('completed');
            } else if (i === step) {
                stepEl.classList.add('active');
            }
        }
        
        if (progressFill && i === 2) {
            progressFill.style.width = step >= 2 ? '66.66%' : '33.33%';
        }
    }
}

// Process payment
function processPayment() {
    const completePaymentBtn = document.getElementById('completePayment');
    if (completePaymentBtn) {
        completePaymentBtn.disabled = true;
        completePaymentBtn.textContent = 'Processing...';
    }
    
    // Simulate payment processing
    setTimeout(() => {
        goToStep(3);
        
        // Set next billing date
        const nextBilling = document.getElementById('nextBilling');
        if (nextBilling) {
            const nextDate = new Date();
            nextDate.setMonth(nextDate.getMonth() + 1);
            nextBilling.textContent = nextDate.toLocaleDateString('en-US', { 
                year: 'numeric', 
                month: 'long', 
                day: 'numeric' 
            });
        }
        
        // Reset button
        if (completePaymentBtn) {
            completePaymentBtn.disabled = false;
            completePaymentBtn.textContent = 'Complete Payment';
        }
    }, 2000);
}

// Initialize form validation
function initializeFormValidation() {
    // Card number formatting
    const cardNumberInput = document.querySelector('input[placeholder*="1234 5678"]');
    if (cardNumberInput) {
        cardNumberInput.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\s/g, '').replace(/[^0-9]/gi, '');
            let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
            e.target.value = formattedValue;
        });
    }
    
    // Expiry date formatting
    const expiryInput = document.querySelector('input[placeholder*="MM/YY"]');
    if (expiryInput) {
        expiryInput.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length >= 2) {
                value = value.substring(0, 2) + '/' + value.substring(2, 4);
            }
            e.target.value = value;
        });
    }
    
    // CVV validation
    const cvvInput = document.querySelector('input[placeholder*="123"]');
    if (cvvInput) {
        cvvInput.addEventListener('input', (e) => {
            e.target.value = e.target.value.replace(/[^0-9]/g, '');
        });
    }
}

// Show content modal
function showContentModal(contentId) {
    const modal = document.getElementById('imageModal');
    const modalImage = document.getElementById('modalImage');
    
    if (modal && modalImage) {
        // Set image source based on content
        const contentImages = {
            'artwork1': 'resources/content-preview.jpg',
            'tutorial1': 'resources/creator-1.jpg',
            'behind1': 'resources/creator-2.jpg',
            'artwork2': 'resources/content-preview.jpg',
            'process1': 'resources/creator-1.jpg',
            'free1': 'resources/creator-2.jpg'
        };
        
        modalImage.src = contentImages[contentId] || 'resources/content-preview.jpg';
        modal.classList.add('active');
        
        // Animate modal
        anime({
            targets: modal.querySelector('.modal-content'),
            scale: [0.8, 1],
            opacity: [0, 1],
            duration: 300,
            easing: 'easeOutQuad'
        });
    }
}

// Show tip modal
function showTipModal() {
    const tipAmount = prompt('Enter tip amount ($):');
    if (tipAmount && !isNaN(tipAmount) && tipAmount > 0) {
        showNotification(`Tip of $${tipAmount} sent to Sophia Chen!`, 'success');
        
        // Animate tip button
        const tipBtn = document.getElementById('tip-btn');
        anime({
            targets: tipBtn,
            scale: [1, 1.2, 1],
            backgroundColor: ['#ffd93d', '#4ade80', '#ffd93d'],
            duration: 600,
            easing: 'easeOutQuad'
        });
    }
}

// Show notification
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `fixed top-20 right-4 z-50 px-6 py-4 rounded-lg text-white font-medium transform translate-x-full transition-transform duration-300`;
    
    // Set background color based on type
    switch(type) {
        case 'success':
            notification.classList.add('bg-green-500');
            break;
        case 'error':
            notification.classList.add('bg-red-500');
            break;
        case 'warning':
            notification.classList.add('bg-yellow-500');
            break;
        default:
            notification.classList.add('bg-blue-500');
    }
    
    notification.textContent = message;
    document.body.appendChild(notification);
    
    // Animate in
    setTimeout(() => {
        notification.classList.remove('translate-x-full');
    }, 100);
    
    // Animate out and remove
    setTimeout(() => {
        notification.classList.add('translate-x-full');
        setTimeout(() => {
            document.body.removeChild(notification);
        }, 300);
    }, 3000);
}

// Utility function to handle responsive design
function handleResize() {
    // Update particle canvas size
    const particlesContainer = document.getElementById('particles-container');
    if (particlesContainer && particlesContainer.querySelector('canvas')) {
        const canvas = particlesContainer.querySelector('canvas');
        canvas.style.width = particlesContainer.offsetWidth + 'px';
        canvas.style.height = particlesContainer.offsetHeight + 'px';
    }
}

// Add resize event listener
window.addEventListener('resize', handleResize);

// Add smooth scrolling for anchor links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

// Add loading animation for page transitions
window.addEventListener('beforeunload', () => {
    document.body.style.opacity = '0.7';
});

// Initialize tooltips for interactive elements
function initializeTooltips() {
    const tooltipElements = document.querySelectorAll('[data-tooltip]');
    
    tooltipElements.forEach(element => {
        const tooltip = document.createElement('div');
        tooltip.className = 'absolute bg-black bg-opacity-80 text-white px-2 py-1 rounded text-sm z-50 pointer-events-none opacity-0 transition-opacity duration-200';
        tooltip.textContent = element.dataset.tooltip;
        
        element.addEventListener('mouseenter', () => {
            document.body.appendChild(tooltip);
            const rect = element.getBoundingClientRect();
            tooltip.style.left = rect.left + (rect.width / 2) - (tooltip.offsetWidth / 2) + 'px';
            tooltip.style.top = rect.top - tooltip.offsetHeight - 5 + 'px';
            tooltip.style.opacity = '1';
        });
        
        element.addEventListener('mouseleave', () => {
            tooltip.style.opacity = '0';
            setTimeout(() => {
                if (tooltip.parentNode) {
                    document.body.removeChild(tooltip);
                }
            }, 200);
        });
    });
}

// Initialize tooltips when DOM is ready
document.addEventListener('DOMContentLoaded', initializeTooltips);

// Export functions for global access
window.CreatorHub = {
    showNotification,
    goToStep,
    showContentModal,
    showTipModal
};